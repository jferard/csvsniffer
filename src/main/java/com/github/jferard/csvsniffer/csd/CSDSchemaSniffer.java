package com.github.jferard.csvsniffer.csd;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.Iterator;
import java.util.logging.Logger;

/**
 * The CSDSchemaSniffer is a sniffer. Given a CSDSchemaPattern, a CSVParser and a maximul line, it will return a
 * CSDSchema (or null).
 *
 */
public class CSDSchemaSniffer<F extends CSDField> {
    private final Logger logger;
    private CSDValidationResult<F> result;
    private CSDFieldFactory factory;
    private CSDValidatorHelper<F> validatorHelper;

    public static <G extends CSDField> CSDSchemaSniffer create(Logger logger, CSDFieldFactory factory) {
        CSDUtil u = new CSDUtil(logger);
        CSDValidatorHelper<G> hv = new CSDValidatorHelper<G>(logger, new FlexibleColumnMatcher(logger, u));
        return new CSDSchemaSniffer(logger, factory, hv);
    }

    public CSDSchemaSniffer(Logger logger, CSDFieldFactory factory, CSDValidatorHelper<F> validatorHelper) {
        this.logger = logger;
        this.factory = factory;
        this.validatorHelper = validatorHelper;
    }

    /**
     * @param schemaPattern the pattern to be tested
     * @param parser the CSVCRecord provider
     * @param maxLine the maximum number of lines
     * @return the real CSDSchema, or null if the pattern does not match.
     */
    public CSDSchema sniff(CSDSchemaPattern<F> schemaPattern, CSVParser parser, int maxLine) {
        this.result = new CSDValidationResult<F>(logger, schemaPattern);
        Iterator<CSVRecord> it = parser.iterator();

        if (!it.hasNext()) {
            result.noLine();
            return null;
        }

        CSVRecord firstRecord = it.next();
        if (!this.validateHeaderOrFirstRecord(result, schemaPattern, firstRecord))
            return null;

        int i=1;
        while (it.hasNext() && i < maxLine)
            this.validatorHelper.validateRecord(result, schemaPattern, it.next(), i++);

        if (this.result.errorCount() > maxLine)
            return null;
        
        return schemaPattern.newSchema(factory, firstRecord);
    }

    private boolean validateHeaderOrFirstRecord(CSDValidationResult<F> result, CSDSchemaPattern<F> schemaPattern, CSVRecord firstRecord) {
        int headerErrorCount = this.validatorHelper.validateHeader(this.result, schemaPattern, firstRecord);

        switch (headerErrorCount) {
            case -1:
                return false;
            case 0:
                return true;
            case 1:
                this.logger.severe("One clear error in header, but it seems to match.");
                return true;
            default:
                if (schemaPattern.hasOptionalHeader()) {
                    this.logger.info("The header does not match. Maybe a missing header.");
                    CSDValidationResult<F> tempResult = new CSDValidationResult<F>(this.logger, schemaPattern);
                    this.validatorHelper.validateRecord(tempResult, schemaPattern, firstRecord, 1);
                    if (tempResult.isOk()) {
                        this.result = tempResult;
                        this.result.missingHeader();
                        return true;
                    } else {
                        this.result.badHeader();
                        return false;
                    }
                } else {
                    return false;
                }
        }

    }
}

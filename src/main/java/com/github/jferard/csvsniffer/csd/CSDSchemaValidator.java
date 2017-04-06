package com.github.jferard.csvsniffer.csd;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.Iterator;
import java.util.logging.Logger;

/**
 * The CSDSchemaSniffer#validateHeader method returns the full result of a validation.
 */
public class CSDSchemaValidator<F extends CSDField> {
    private final Logger logger;
    private final CSDValidatorHelper<F> validatorHelper;
    private CSDValidationResult<F> result;

    public static <G extends CSDField> CSDSchemaValidator create(Logger logger) {
        CSDValidatorHelper<G> hv = new CSDValidatorHelper<G>(logger, new ExactColumnMatcher(logger));
        return new CSDSchemaValidator(logger, hv);
    }

    public CSDSchemaValidator(Logger logger, CSDValidatorHelper<F> validatorHelper) {
        this.logger = logger;
        this.validatorHelper = validatorHelper;
    }

    /**
     * @param schema the schema to be tested
     * @param parser the CSVCRecord provider
     * @return a validation result
     */
    public CSDValidationResult validate(CSDSchema<F> schema, CSVParser parser) {
        this.result = new CSDValidationResult<F>(logger, schema);
        Iterator<CSVRecord> it = parser.iterator();
        if (it.hasNext()) {
            CSVRecord firstRecord = it.next();
            this.validateHeaderOrFirstRecord(schema, firstRecord);
            int i=1;
            while (it.hasNext())
                this.validatorHelper.validateRecord(result, schema, it.next(), i++);

        } else {
            result.noLine();
        }
        return this.result;
    }

    private void validateHeaderOrFirstRecord(CSDSchema<F> schema, CSVRecord firstRecord) {
        int headerErrorCount = this.validatorHelper.validateHeader(this.result, schema, firstRecord);
        if (headerErrorCount > 1 && schema.hasOptionalHeader()) {
            CSDValidationResult<F> tempResult = new CSDValidationResult<F>(this.logger, schema);
            this.validatorHelper.validateRecord(tempResult, schema, firstRecord, 1);
            if (tempResult.isOk()) {
                this.result = tempResult;
                this.result.missingHeader();
            } else {
                this.result.badHeader();
            }
        }
    }
}
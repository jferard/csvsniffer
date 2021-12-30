# CSV Sniffer
(C) J. Férard 2016-2017, 2021

A simple sniffer to detect file encoding and CSV format of a file, under GPL v3.

*Nota: The old version of this project is still in use in https://github.com/jferard/pgloaderutils.*

License: GPLv3

[![Build Status](https://app.travis-ci.com/jferard/csvsniffer.svg?branch=master)](https://travis-ci.com/github/jferard/csvsniffer)
[![Code Coverage](https://img.shields.io/codecov/c/github/jferard/csvsniffer/master.svg)](https://codecov.io/github/jferard/csvsniffer?branch=master)


## Introduction
The goal of this sniffer is to detect, for a given **sane** CSV file :
- the encoding;
- the delimiter char, quote char and escape char;
- the type of the columns.

The "sane" adjective is important: the goal is to handle *automatically generated* and *correct* CSV files, not to guess the format of a manually created file or a flawed file.

### CSVSniffer
The encoding is detected using chardet. The CSV sniffer is a simple sniffer. It performs a first try with quotes (`"`) and a second try without. There is a little parser that ignores the quoted delimiters and finds the escape char.

Roughly, the delimiter is the most evenly distributed char, quote excluded. 

*The CSVSniffer may fail on weird CSV files.*

### MetaCSVSniffer
The goal is to find automatically the type of the CSV columns. This is useful when you need to create an SQL table or an ODS file, or to make a computation. 

*The format should be checked (and maybe tuned) manually.*

The MetaCSVSniffer validate each column against some predefined formats: this is slow but reliable. The slowness is not an issue here, since you'll have to check the result anyway.

You can test the MetaCSVSniffer using the exec-maven-plugin.
```
csvsniffer$ mvn -q compile exec:java -Dexec.mainClass="com.github.jferard.csvsniffer.MetaCSVSniffer" -Dexec.args="src/test/resources/20201001-bal-216402149.csv"
```

Output:

```
domain,key,value
file,encoding,UTF-8
file,bom,false
file,line_terminator,\r\n
csv,delimiter,;
csv,double_quote,false
csv,escape_char,""
csv,quote_char,""
csv,skip_initial_space,false
data,null_value,
data,col/3/type,integer
data,col/7/type,float//.
data,col/8/type,float//.
data,col/9/type,float//.
data,col/10/type,float//.
data,col/12/type,date/yyyy-MM-dd
```

(The MetaCSVSniffer output is a [MetaCSV](https://github.com/jferard/MetaCSV) file.) 
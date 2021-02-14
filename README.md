[![Build Status](https://travis-ci.org/jferard/csvsniffer.svg?branch=master)](https://travis-ci.org/jferard/csvsniffer)
[![Code Coverage](https://img.shields.io/codecov/c/github/jferard/csvsniffer/master.svg)](https://codecov.io/github/jferard/csvsniffer?branch=master)

# CSV Sniffer
(C) J. Férard 2016-2017, 2021

A simple sniffer to detect file encoding and CSV format of a file, under GPL v3.

*Nota: The old version of this project is still in use in https://github.com/jferard/pgloaderutils.*

## Introduction

The goal of this sniffer is to detect, for a given **sane** CSV file :
- the encoding;
- the delimiter char, quote char and escape char;

CSVSniffer uses [MetaCSV](https://github.com/jferard/MetaCSV) format.
# CSV Sniffer
(C) J. Férard 2016-2017, 2021

A simple sniffer to detect file encoding and CSV format of a file, under GPL v3.

*Nota: The old version of this project is still in use in https://github.com/jferard/pgloaderutils.*

## Introduction

The goal of this sniffer is to detect, for a given **sane** CSV file :
- the encoding;
- the delimiter char, quote char and escape char;

CSVSniffer uses [MetaCSV](https://github.com/jferard/MetaCSV) format.
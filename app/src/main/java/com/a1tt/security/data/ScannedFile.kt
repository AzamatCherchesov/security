package com.a1tt.security.data

class ScannedFile(val scannedFile: String, val scanDate: String, val verboseMsg: String, val numberPositives: Int, val numberTotal: Int,
                  val scans: MutableList<FileScanServicesResult>)

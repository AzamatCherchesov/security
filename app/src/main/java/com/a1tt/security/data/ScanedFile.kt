package com.a1tt.security.data

class ScanedFile (val scanedFile: String, val scanDate: String, val verboseMsg: String, val numberPositives: Int, val numberTotal: Int,
                  val scans: MutableList<FileScanServicesResult>)

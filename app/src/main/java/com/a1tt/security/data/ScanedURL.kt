package com.a1tt.security.data

data class ScanedURL(val scanedURL: String, val scanDate: String, val verboseMsg: String, val numberPositives: Int, val numberTotal: Int,
                     val scans: Set<Pair<String, ServicesResult>>)

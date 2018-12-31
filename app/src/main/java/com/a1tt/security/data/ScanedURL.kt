package com.a1tt.security.data

import com.a1tt.security.data.ServicesResult

data class ScanedURL(val scanedURL: String, val scanDate: String, val verboseMsg: String, val numberPositives: Int, val numberTotal: Int,
                     val scans: Set<Pair<String, ServicesResult>>)

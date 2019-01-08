package com.a1tt.security.data

data class ScannedURL(val scannedURL: String, val scanDate: String, val verboseMsg: String, val numberPositives: Int, val numberTotal: Int,
                      val scans: MutableList<URLScanServicesResult>)

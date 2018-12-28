package com.a1tt.security

interface Observer <T> {

     fun objectCreated(t: T)
     fun objectModified(t: T)
     fun objectRemoved (t: T)
}
var expected = "expected";

if (value == expected)
    null; // Success
else if (value.length < expected.length && value == "expected".substring(0,value.length))
    "partial"; // Report partial result
else
    "Invalid value '" + value + "'";

package com.scuilion.confluence.macro;

import java.nio.charset.Charset;

public enum SupportedFileEncoding
{
  UTF_8("UTF-8"),  US_ASCII("US-ASCII"),  ISO_8859_1("ISO-8859-1"),  UTF_16BE("UTF-16BE"),  UTF_16LE("UTF-16LE"),  UTF_16("UTF-16");
  
  private final String charsetName;
  
  private SupportedFileEncoding(String charsetName)
  {
    this.charsetName = charsetName;
  }
  
  public static SupportedFileEncoding byCharset(String charset)
  {
    if (charset == null) {
      return getDefaultEncoding();
    }
    for (SupportedFileEncoding v : values()) {
      if (v.charsetName.equals(charset)) {
        return v;
      }
    }
    return getDefaultEncoding();
  }
  
  public static SupportedFileEncoding getDefaultEncoding()
  {
    return UTF_8;
  }
  
  public Charset getCharset()
  {
    return Charset.forName(this.charsetName);
  }
}
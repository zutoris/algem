/*
 * @(#)WebSite.java	2.9.7 12/05/16
 *
 * Copyright (c) 1999-2016 Musiques Tangentes. All Rights Reserved.
 *
 * This file is part of Algem.
 * Algem is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Algem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Algem. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.algem.contact;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.7
 * @since 2.0r
 */
public class WebSite
        implements java.io.Serializable, Cloneable
{

  public static final int DEFAULT_TYPE = 1;
  public static final String FACEBOOK_PREFIX = "https://www.facebook.com/";
  public static final String MYSPACE_PREFIX = "https://www.myspace.com/";
  public static final String TWITTER_PREFIX = "https://twitter.com/";
  public static final String HTTP_PREFIX = "http://";
  public static final String HTTPS_PREFIX = "https://";
  private static final long serialVersionUID = -2440352101883348803L;
  
  private int idx;
  private int idper;
  private String url;
  private int type;
  private short ptype;

  public WebSite() {
  }

  public WebSite(int key, String _url) {
    type = key;
    url = _url;
  }

  public int getIdx() {
    return idx;
  }

  public void setIdx(int wid) {
    this.idx = wid;
  }

  public int getIdper() {
    return idper;
  }

  public void setIdper(int idper) {
    this.idper = idper;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public short getPtype() {
    return ptype;
  }

  public void setPtype(short ptype) {
    this.ptype = ptype;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final WebSite other = (WebSite) obj;
    if (this.idx != other.idx) {
      return false;
    }
    if (this.idper != other.idper) {
      return false;
    }
    if (this.ptype != other.ptype) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + this.idx;
    hash = 53 * hash + this.idper;
    hash = 53 * hash + this.ptype;
    return hash;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public boolean equiv(WebSite s) {
    // s.getUrl() may not include prefixed protocol (http://, http://www.)
    return idx == s.getIdx() && url.endsWith(s.getUrl()) && type == s.getType();
  }

  @Override
  public String toString() {
    return url;
  }
  
}

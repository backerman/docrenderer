<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:fo="http://www.w3.org/1999/XSL/Format" 
  xmlns:xs="http://www.w3.org/2001/XMLSchema">

  <!-- Specify the font to be used for this document. -->
  <xsl:param name="font">Palatino</xsl:param>

  <xsl:template match="foos">
    <fo:root 
      xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="foo" page-width="3.25in"
                               page-height="3.75in">
          <fo:region-body margin="2.5mm"/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <xsl:apply-templates/>
    </fo:root>
  </xsl:template>

  <!-- Draw a checkbox; check it iff the "checked" parameter is true. -->
  <xsl:template name="checkbox">
    <xsl:param name="size">11</xsl:param>
    <xsl:param name="checked"/>
    <fo:instream-foreign-object>
      <svg:svg 
        xmlns:svg="http://www.w3.org/2000/svg" width="{$size}pt" height="11pt">
        <svg:g>
          <svg:path d="M 0,0 L 0,11 L 11,11 L 11,0 L 0,0 Z"
                    style="fill:none;stroke:black;stroke-width:0.25mm;"/>
          <xsl:if test="$checked">
            <svg:path d="M 0.4,0.4 L 10.6,10.6 M 0.4,10.6 L 10.6,0.4"
                      stroke-linecap="round"
                      style="fill:none;stroke:black;stroke-width:0.5mm"/>
          </xsl:if>
        </svg:g>
      </svg:svg>
    </fo:instream-foreign-object>
  </xsl:template>

  <xsl:template name="test_xslt2">
    <xsl:param name="num_numbers">3</xsl:param>
    <xsl:param name="blank_row_end">true</xsl:param>
    <fo:table table-layout="fixed" block-progression-dimension="auto"
              width="100%">
      <fo:table-column column-width="60%" border="solid 0.5pt"/>
      <fo:table-column column-width="20%" border="solid 0.5pt"/>
      <fo:table-column column-width="20%" border="solid 0.5pt"/>
      <fo:table-header>
        <fo:table-row border-after-style="solid" border-after-width="1pt">
          <fo:table-cell>
            <fo:block font-size="9pt" margin-left="0.1em" margin-top="0.1em"
              font-family="{$font}">
              Name
            </fo:block>
          </fo:table-cell>
          <fo:table-cell>
            <fo:block font-size="9pt" margin-left="0.1em" margin-top="0.1em"
              font-family="{$font}">
              #
            </fo:block>
          </fo:table-cell>
          <fo:table-cell>
            <fo:block font-size="9pt" margin-left="0.1em" margin-top="0.1em"
              font-family="{$font}">
              $
            </fo:block>
          </fo:table-cell>
        </fo:table-row>
      </fo:table-header>
      <fo:table-body>
        <xsl:for-each select="1 to $num_numbers cast as xs:integer + 
                              (if ($blank_row_end) then 1 else 0)">
          <fo:table-row height="7mm" border="solid 0.5pt">
            <fo:table-cell>
              <fo:block font-size="5pt" font-family="{$font}">
                <xsl:if test="position() &lt;= $num_numbers">
                  <xsl:value-of select="."/>)
                </xsl:if>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block></fo:block>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block></fo:block>
            </fo:table-cell>
          </fo:table-row>
        </xsl:for-each>
      </fo:table-body>
    </fo:table>
  </xsl:template>

  <xsl:template match="foo">
    <fo:page-sequence master-reference="foo">
      <fo:flow flow-name="xsl-region-body">
        <fo:table width="100%" height="100%" border-collapse="collapse"
                  table-layout="fixed" block-progression-dimension="auto">
          <fo:table-column column-width="80%" border="solid 0.5pt"/>
          <fo:table-column column-width="20%" border="solid 0.5pt"/>
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell number-columns-spanned="2">
                <fo:block text-align="center" font-size="14pt"
                          font-weight="bolder" margin-top="5pt"
                          margin-bottom="5pt" font-family="{$font}">
                  docrenderer test
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row height="8mm" border="solid 0.5pt">
              <fo:table-cell>
                <fo:block font-size="5pt" text-align="start"
                          font-family="{$font}">
                  FOO
                </fo:block>
                <fo:block font-size="10pt" font-family="{$font}"
                          margin-left="0.2em">
                  <xsl:value-of select="bar/name"/>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell>
                <fo:block font-size="5pt" text-align="start"
                          font-family="{$font}">
                  BAR
                </fo:block>
                <fo:block font-size="10pt" font-family="{$font}"
                          margin-left="0.2em">
                  <xsl:value-of select="bar/@number"/>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row height="8mm" border="solid 0.5pt">
              <fo:table-cell>
                <fo:block font-size="5pt" text-align="start"
                          font-family="{$font}">
                  BAZ
                </fo:block>
                <fo:block font-size="10pt" font-family="{$font}"
                          margin-left="0.2em">
                  <xsl:value-of select="baz/name"/>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell>
                <fo:block font-size="5pt" text-align="start"
                          font-family="{$font}">
                  QUUX
                </fo:block>
                <fo:block font-size="10pt" font-family="{$font}"
                          margin-left="0.2em">
                  <xsl:value-of select="baz/@number"/>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row height="8mm" border="solid 0.5pt">
              <fo:table-cell number-columns-spanned="2">
                <fo:block font-size="5pt" text-align="start"
                          font-family="{$font}">
                  MAGIC
                </fo:block>
                <fo:block font-size="10pt" font-family="{$font}"
                          margin-left="0.2em">
                  <xsl:value-of select="bar/baz"/>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
          </fo:table-body>
        </fo:table>
        <fo:table table-layout="fixed" block-progression-dimension="auto"
                  width="100%">
          <fo:table-column column-width="70%" border="solid 0.5pt"/>
          <fo:table-column column-width="30%" border="solid 0.5pt"/>
          <fo:table-body>
            <fo:table-row height="18pt" border="solid 1pt">
              <fo:table-cell display-align="center">
                <fo:block text-align="right" margin-right="0.5em"
                          font-size="10pt" font-family="{$font}">
                Chaos
              </fo:block>
              </fo:table-cell>
              <fo:table-cell display-align="center">
                <fo:block margin-left="0.5em" font-size="10pt"
                          font-family="{$font}">
                  Left
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row height="18pt" border="solid 1pt"
                          border-after-style="double"
                          border-after-width="2pt">
              <fo:table-cell display-align="center">
                <fo:block text-align="right" margin-right="0.5em"
                          font-size="10pt" font-family="{$font}">
                  Order
                </fo:block>
              </fo:table-cell>
              <fo:table-cell display-align="center">
                <fo:block margin-left="0.5em" font-size="10pt"
                          font-family="{$font}">
                  Right
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
          </fo:table-body>
        </fo:table>
        <xsl:call-template name="test_xslt2"/>
        <fo:table table-layout="fixed" block-progression-dimension="auto"
                  width="100%">
          <fo:table-column column-width="20%"/>
          <fo:table-column column-width="20%"/>
          <fo:table-column column-width="20%"/>
          <fo:table-column column-width="20%"/>
          <fo:table-column column-width="20%"/>
          <fo:table-body>
            <fo:table-row height="7mm" border="solid 1pt">
              <fo:table-cell display-align="center">
                <fo:block font-size="6pt" font-weight="bold"
                          margin-left="0.5em">
                  TEST
                </fo:block>
                <fo:block font-size="6pt" font-weight="bold"
                          margin-left="0.5em">
                  USE ONLY:
                </fo:block>
              </fo:table-cell>
              <fo:table-cell display-align="center">
                <fo:block>
                  <xsl:call-template name="checkbox"/>
                  <fo:inline font-size="8pt" alignment-adjust="50%"
                             alignment-baseline="after-edge">
                    A
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell display-align="center">
                <fo:block>
                  <xsl:call-template name="checkbox"/>
                  <fo:inline font-size="8pt" alignment-adjust="50%"
                             alignment-baseline="after-edge">
                    B
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell display-align="center">
                <fo:block>
                  <xsl:call-template name="checkbox"/>
                  <fo:inline font-size="8pt" alignment-adjust="50%"
                             alignment-baseline="after-edge">
                    C
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell display-align="center">
                <fo:block>
                  <xsl:call-template name="checkbox">
                    <xsl:with-param name="checked" select="bar/checked"/>
                  </xsl:call-template>
                  <fo:inline font-size="8pt" alignment-adjust="50%"
                             alignment-baseline="after-edge">
                    D
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
          </fo:table-body>
        </fo:table>
      </fo:flow>
    </fo:page-sequence>
  </xsl:template>
</xsl:stylesheet>

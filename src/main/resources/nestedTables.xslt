<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output omit-xml-declaration="yes" indent="yes"/>
<xsl:strip-space elements="*"/>

<xsl:template match="/">
<table>
<xsl:apply-templates/>
</table>
</xsl:template>

<xsl:template match="*">
<tr>
<td style="vertical-align: top;">
<xsl:value-of select="name()"/>
</td>
<td style="vertical-align: top;">
<xsl:value-of select="."/> 
<xsl:value-of select="@*"/>
</td>
</tr>
</xsl:template>

<xsl:template match="*[*]">
<tr>
<td style="vertical-align: top;">
<xsl:value-of select="name()"/>
</td>
<td style="vertical-align: top;">
<table>
<xsl:apply-templates/>
</table>
</td>
</tr>
</xsl:template>

</xsl:stylesheet>

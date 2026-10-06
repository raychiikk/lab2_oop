<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes"/>

    <xsl:key name="depositsByType" match="Deposit" use="Type" />

    <xsl:template match="/Bank">
        <GroupedBank>
            <!-- Проходимо по унікальних типах вкладів -->
            <xsl:for-each select="Deposit[generate-id() = generate-id(key('depositsByType', Type)[1])]">
                <DepositGroup>
                    <xsl:attribute name="type">
                        <xsl:value-of select="Type"/>
                    </xsl:attribute>
                    
                    <!-- Виводимо всі депозити, що належать до цього типу -->
                    <xsl:for-each select="key('depositsByType', Type)">
                        <Deposit accountId="{@accountId}">
                            <Name><xsl:value-of select="Name"/></Name>
                            <Country><xsl:value-of select="Country"/></Country>
                            <Depositor><xsl:value-of select="Depositor"/></Depositor>
                            <AmountOnDeposit><xsl:value-of select="AmountOnDeposit"/></AmountOnDeposit>
                            <Profitability><xsl:value-of select="Profitability"/></Profitability>
                            <TimeConstraints><xsl:value-of select="TimeConstraints"/></TimeConstraints>
                        </Deposit>
                    </xsl:for-each>
                </DepositGroup>
            </xsl:for-each>
        </GroupedBank>
    </xsl:template>
</xsl:stylesheet>
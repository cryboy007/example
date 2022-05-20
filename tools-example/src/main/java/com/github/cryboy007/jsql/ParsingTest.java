package com.github.cryboy007.jsql;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.AddAliasesVisitor;
import net.sf.jsqlparser.util.SelectUtils;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.List;

/**
 * @ClassName ParsingTest
 * @Author tao.he
 * @Since 2022/5/20 15:08
 */
public class ParsingTest {
    public static void main(String[] args) {
        //parserStatement();

        //getTables();

        //setAliases();

        //addColumn();



    }

    private static void addColumn() {
        try {
            Select select = (Select) CCJSqlParserUtil.parse("select a from mytable");
            SelectUtils.addExpression(select,new Column("b"));
            System.out.println(select.getSelectBody());
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * SELECT a AS A1, b AS A2, c AS A3 FROM test
     */
    private static void setAliases() {
        Select select = null;
        try {
            select = (Select) CCJSqlParserUtil.parse("select a,b,c from test");
            final AddAliasesVisitor instance = new AddAliasesVisitor();
            select.getSelectBody().accept(instance);
            System.out.println(select.getSelectBody());
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    private static void getTables() {
        try {
            Statement statement = CCJSqlParserUtil.parse("SELECT * FROM MY_TABLE1");
            Select selectStatement = (Select) statement;

            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            final List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
            System.out.println("tableNames = " + tableList);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    private static void parserStatement() {
        try {
            //单个语句
            Statement stmt = CCJSqlParserUtil.parse("SELECT * FROM tab1");
            //多个
            Statements stmts = CCJSqlParserUtil.parseStatements("SELECT * FROM tab1; SELECT * FROM tab2");

        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }
}

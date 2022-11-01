package com.github.cryboy007.jsql;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.SelectUtils;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

/**
 * @ClassName SqlBuilding
 * @Author tao.he
 * @Since 2022/5/20 15:28
 */
public class SqlBuilding {
    public static void main(String[] args) throws JSQLParserException {
        //buildSelect();

        //buildInsert();

        //replaceString();

        bulidingPart2();

    }

    private static void bulidingPart2() throws JSQLParserException {

        Select stmt = (Select) CCJSqlParserUtil.parse("SELECT col1 AS a, col2 AS b, col3 AS c FROM table WHERE col_1 = 10 AND col_2 = 20 AND col_3 = 30");
        System.out.println("before " + stmt.toString());

        final PlainSelect selectBody = (PlainSelect) stmt.getSelectBody();
        selectBody.getWhere().accept(new ExpressionVisitorAdapter(){
            @Override
            public void visit(Column column) {
                column.setColumnName(column.getColumnName().replace("_", ""));
            }
        });
        System.out.println("after " + stmt.toString());

    }

    private static void replaceString() {
        String sql ="SELECT NAME, ADDRESS, COL1 FROM USER WHERE SSN IN ('11111111111111', '22222222222222');";
        Select select = null;
        try {
            select = (Select) CCJSqlParserUtil.parse(sql);

//Start of value modification
            StringBuilder buffer = new StringBuilder();
            ExpressionDeParser expressionDeParser = new ExpressionDeParser() {

                @Override
                public void visit(StringValue stringValue) {
                    System.out.println(stringValue);
                    if ("22222222222222".equals(stringValue.getValue())) {
                        this.getBuffer().append("XXXX");
                    }
                }

            };
            SelectDeParser deparser = new SelectDeParser(expressionDeParser,buffer );
            expressionDeParser.setSelectVisitor(deparser);
            expressionDeParser.setBuffer(buffer);
            select.getSelectBody().accept(deparser);
//End of value modification


            System.out.println(buffer.toString());
//Result is: SELECT NAME, ADDRESS, COL1 FROM USER WHERE SSN IN (XXXX, XXXX)
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    private static void buildInsert() {
        try {
            final Insert insert = (Insert) CCJSqlParserUtil.parse("insert into mt_blog (col1) values (1)");
            insert.getColumns().add(new Column("b"));
            //添加值
            insert.getItemsList().accept(new ItemsListVisitor() {
                @Override
                public void visit(SubSelect subSelect) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void visit(ExpressionList expressionList) {
                    expressionList.getExpressions().add(new LongValue(5));
                }

                @Override
                public void visit(NamedExpressionList namedExpressionList) {
                }

                @Override
                public void visit(MultiExpressionList multiExprList) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
            System.out.println(insert.toString());

            insert.getColumns().add(new Column("c"));
            final ExpressionList itemsList = (ExpressionList) insert.getItemsList();
            itemsList.getExpressions().add(new LongValue(20));
            System.out.println(insert.toString());
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

    }

    private static void buildSelect() {
        final Select select = SelectUtils.buildSelectFromTable(new Table("mt_blog"));
        final Select select1 = SelectUtils.buildSelectFromTableAndExpressions(new Table("mt_blog"),
                new Column("a"), new Column("b"));

        try {
            //不构建 标准的语法树
            final Select select3 = SelectUtils.buildSelectFromTableAndExpressions(new Table("mt_blog"), "a+b", "c");
            System.out.println("select = " + select);
            System.out.println("select1 = " + select1);
            System.out.println("select3 = " + select3);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }


    }
}

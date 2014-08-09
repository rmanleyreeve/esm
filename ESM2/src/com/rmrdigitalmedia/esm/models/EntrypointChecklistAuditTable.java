package com.rmrdigitalmedia.esm.models ;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.javaranch.common.Str;
import com.javaranch.db.DBResults;
import com.javaranch.db.TableFacade;

/** Strongly typed access to the database table "ENTRYPOINT_CHECKLIST_AUDIT".
 *
 * This source file was automatically generated by "Jenny the db code generator"
 * based on information found in the database.  Do not modify this file!
 *
 * For more information on Jenny, see http://www.javaranch.com/jenny.jsp
 *
 *
 * Most of the methods are static so you don't need to instantiate a copy of this class 
 * to do your work.  The primary access methods are:
 * <ul>
 *
 *     <b>getRow()/getRows()/getAllRows()</b><br>
 *     <b>search() </b><i>like getRows(), but you can specify which columns you want back</i><br>
 *     <b>update()</b><br>
 *     <b>delete()</b><br>
 *     <b>insert()</b><br>
 *
 * </ul>
 *
 * These methods all have the option of passing in a connection as the first parameter.
 * Usually you won't use a connection directly, but sometimes it's useful.
 *
 * The getRows() methods all return an array of Row objects or a single Row object.  The
 * row object is easy to work with and provides strong type checking.  If your table has
 * a lot of columns, and your search will return a lot of rows, you might want to consider
 * using a search() method instead.  You lose some of your strong type checking, but 
 * you might go a lot easier on memory.  In these cases, you will want to make sure you
 * use the column name constants found at the top of this class.
 *
 */
public class EntrypointChecklistAuditTable
{

    private static Implementation imp = new Implementation();

    public static final String tableName = "ENTRYPOINT_CHECKLIST_AUDIT";

    public static final String idColumnName = "ID";
    public static final String entrypointIDColumnName = "ENTRYPOINT_ID";
    public static final String q1ValueColumnName = "Q1_VALUE";
    public static final String q1CommentsColumnName = "Q1_COMMENTS";
    public static final String q2BooleanColumnName = "Q2_BOOLEAN";
    public static final String q2CommentsColumnName = "Q2_COMMENTS";
    public static final String q3BooleanColumnName = "Q3_BOOLEAN";
    public static final String q3CommentsColumnName = "Q3_COMMENTS";
    public static final String q4ValueColumnName = "Q4_VALUE";
    public static final String q4CommentsColumnName = "Q4_COMMENTS";
    public static final String q5DimsHColumnName = "Q5_DIMS_H";
    public static final String q5DimsWColumnName = "Q5_DIMS_W";
    public static final String q5CommentsColumnName = "Q5_COMMENTS";
    public static final String q6BooleanColumnName = "Q6_BOOLEAN";
    public static final String q6CommentsColumnName = "Q6_COMMENTS";
    public static final String q7ValueColumnName = "Q7_VALUE";
    public static final String q7CommentsColumnName = "Q7_COMMENTS";
    public static final String q8BooleanColumnName = "Q8_BOOLEAN";
    public static final String q8CommentsColumnName = "Q8_COMMENTS";
    public static final String q9BooleanColumnName = "Q9_BOOLEAN";
    public static final String q9CommentsColumnName = "Q9_COMMENTS";
    public static final String q10BooleanColumnName = "Q10_BOOLEAN";
    public static final String q10CommentsColumnName = "Q10_COMMENTS";
    public static final String q11BooleanColumnName = "Q11_BOOLEAN";
    public static final String q11CommentsColumnName = "Q11_COMMENTS";
    public static final String q12BooleanColumnName = "Q12_BOOLEAN";
    public static final String q12CommentsColumnName = "Q12_COMMENTS";
    public static final String q13BooleanColumnName = "Q13_BOOLEAN";
    public static final String q13CommentsColumnName = "Q13_COMMENTS";
    public static final String q14BooleanColumnName = "Q14_BOOLEAN";
    public static final String q14CommentsColumnName = "Q14_COMMENTS";
    public static final String q15BooleanColumnName = "Q15_BOOLEAN";
    public static final String q15CommentsColumnName = "Q15_COMMENTS";
    public static final String q16BooleanColumnName = "Q16_BOOLEAN";
    public static final String q16CommentsColumnName = "Q16_COMMENTS";

    private static String[] allColumns =
    {
        idColumnName , entrypointIDColumnName , q1ValueColumnName , q1CommentsColumnName , q2BooleanColumnName , q2CommentsColumnName , q3BooleanColumnName , q3CommentsColumnName , q4ValueColumnName , q4CommentsColumnName , q5DimsHColumnName , q5DimsWColumnName , q5CommentsColumnName , q6BooleanColumnName , q6CommentsColumnName , q7ValueColumnName , q7CommentsColumnName , q8BooleanColumnName , q8CommentsColumnName , q9BooleanColumnName , q9CommentsColumnName , q10BooleanColumnName , q10CommentsColumnName , q11BooleanColumnName , q11CommentsColumnName , q12BooleanColumnName , q12CommentsColumnName , q13BooleanColumnName , q13CommentsColumnName , q14BooleanColumnName , q14CommentsColumnName , q15BooleanColumnName , q15CommentsColumnName , q16BooleanColumnName , q16CommentsColumnName , 
    };

    /** You probably want to use the static methods for most of your access, but once in a while you might need to
     *  pass an instance object to a method that knows how to work with these sorts of tables.
     */
    public static Implementation getInstance()
    {
        return imp ;
    }

    /** For use by unit testing, although you could provide your own implementation here if
     *  you wanted to.
     *  
     *  To use this in your unit testing, create an instance of MockEntrypointChecklistAuditTable and pass
     *  it in here.  Then set your mock return values, call the method you are testing and examine
     *  the mock values that are now set!
     */
    public static void setInstance( EntrypointChecklistAuditTable.Implementation instance  )
    {
        imp = instance ;
    }

    /** Exposed for unit testing purposes only! */
    static class Implementation extends TableFacade
    {

        /** Exposed for unit testing purposes only! */
        Implementation()
        {
            super( EsmFacade.getInstance() , tableName );
        }

        // convert a DBResults object to an array of Row objects.
        // requires that all of the columns be represented in the DBResults object and in the right order
        private static Row[] rowArray( DBResults r )
        {
            Row[] rows = new Row[ r.size() ];
            for( int i = 0 ; i < rows.length ; i++ )
            {
                rows[ i ] = new Row( r.getRow( i ) );
            }
            return rows ;
        }

        /** Instantiate an empty Row object */
        public Row getRow()
        {
            // if you are wondering about why this method is so lame - it's for unit testing!
            // The idea is that during unit testing, a different test object will be returned here.
            // To learn more about unit testing with Jenny generated code, visit <a href="http://www.javaranch.com/jenny.jsp">www.javaranch.com/jenny.jsp</a>
            return new Row();
        }

        /** Instantiate a Row object and fill its content based on a search for the ID. 
         *
         * Return null if not found.  Return first item if more than one found.
         */
        public Row getRow( Connection con , int id ) throws SQLException
        {
            Row row = new Row( this.search( con , "ID" , String.valueOf( id ) , allColumns ) );
            return row.dataLoadedFromDatabase() ? row : null ;
        }

        /** Instantiate a Row object and fill its content based on a search for the ID.
         *
         * Return null if not found.
         */
        public Row getRow( long id ) throws SQLException
        {
            Row row = new Row( this.search( "ID" , String.valueOf( id ) , allColumns ) );
            return row.dataLoadedFromDatabase() ? row : null ;
        }

        /** Instantiate a Row object and fill its content based on a search
         *
         * Return null if not found.
         */
        public Row getRow( Connection con , String column , String searchText ) throws SQLException
        {
            Row row = new Row( this.search( con , column , searchText , allColumns ) );
            return row.dataLoadedFromDatabase() ? row : null ;
        }

        /** Instantiate a Row object and fill its content based on a search
         *
         * Return null if not found.
         */
        public Row getRow( String column , String searchText ) throws SQLException
        {
            Row row = new Row( this.search( column , searchText , allColumns ) );
            return row.dataLoadedFromDatabase() ? row : null ;
        }

        /** Return an array of length zero if nothing found */
        public Row[] getRows( Connection con , String column , String searchText ) throws SQLException
        {
            return rowArray( this.search( con , column , searchText , allColumns ) );
        }

        /** Return an array of length zero if nothing found */
        public Row[] getRows( String column , String searchText ) throws SQLException
        {
            return rowArray( this.search( column , searchText , allColumns ) );
        }

        /** Return an array of length zero if nothing found */
        public Row[] getRows( Connection con , String column , String[] searchText ) throws SQLException
        {
            return rowArray( this.search( con , column , searchText , allColumns ) );
        }

        /** Return an array of length zero if nothing found */
        public Row[] getRows( String column , String[] searchText ) throws SQLException
        {
            return rowArray( this.search( column , searchText , allColumns ) );
        }

        /** Return an array of length zero if nothing found */
        public Row[] getRows( Connection con , String whereClause ) throws SQLException
        {
            return rowArray( this.search( con , whereClause , allColumns ) );
        }

        /** Return an array of length zero if nothing found */
        public Row[] getRows( String whereClause ) throws SQLException
        {
            return rowArray( this.search( whereClause , allColumns ) );
        }

        /** Return an array of length zero if nothing found */
        public Row[] getAllRows( Connection con ) throws SQLException
        {
            return rowArray( this.search( con , allColumns ) );
        }

        /** Return an array of length zero if nothing found */
        public Row[] getAllRows() throws SQLException
        {
            return rowArray( this.search( allColumns ) );
        }

        public void update( Connection con , int id , Map data ) throws SQLException
        {
            this.update( con , "ID" , String.valueOf( id ) , data );
        }

        public void update( int id , Map data ) throws SQLException
        {
            this.update( "ID" , String.valueOf( id ) , data );
        }

        public void delete( Connection con , long id ) throws SQLException
        {
            this.delete( con , "ID" , String.valueOf( id ) );
        }

        public void delete( long id ) throws SQLException
        {
            this.delete( "ID" , String.valueOf( id ) );
        }

        public long insertAndGetID( Connection con , Map data ) throws SQLException
        {
            return this.insertAndGetID( con , data , "ID" );
        }

        public long insertAndGetID( Map data ) throws SQLException
        {
            return this.insertAndGetID( data , "ID" );
        }


    }

    public static class Row
    {

        private boolean dataLoadedFromDatabase = false ;

        private int id ;
        private int entrypointID ;
        private String q1Value ;
        private String q1Comments ;
        private String q2Boolean ;
        private String q2Comments ;
        private String q3Boolean ;
        private String q3Comments ;
        private String q4Value ;
        private String q4Comments ;
        private String q5DimsH ;
        private String q5DimsW ;
        private String q5Comments ;
        private String q6Boolean ;
        private String q6Comments ;
        private String q7Value ;
        private String q7Comments ;
        private String q8Boolean ;
        private String q8Comments ;
        private String q9Boolean ;
        private String q9Comments ;
        private String q10Boolean ;
        private String q10Comments ;
        private String q11Boolean ;
        private String q11Comments ;
        private String q12Boolean ;
        private String q12Comments ;
        private String q13Boolean ;
        private String q13Comments ;
        private String q14Boolean ;
        private String q14Comments ;
        private String q15Boolean ;
        private String q15Comments ;
        private String q16Boolean ;
        private String q16Comments ;

        /** for internal use only!   If you need a row object, use getRow(). */
        Row()
        {
        }

        private Row( String[] data )
        {
            if ( data != null )
            {
                this.id =  Str.toInt( data[0] );
                this.entrypointID =  Str.toInt( data[1] );
                this.q1Value = data[2];
                this.q1Comments = data[3];
                this.q2Boolean = data[4];
                this.q2Comments = data[5];
                this.q3Boolean = data[6];
                this.q3Comments = data[7];
                this.q4Value = data[8];
                this.q4Comments = data[9];
                this.q5DimsH = data[10];
                this.q5DimsW = data[11];
                this.q5Comments = data[12];
                this.q6Boolean = data[13];
                this.q6Comments = data[14];
                this.q7Value = data[15];
                this.q7Comments = data[16];
                this.q8Boolean = data[17];
                this.q8Comments = data[18];
                this.q9Boolean = data[19];
                this.q9Comments = data[20];
                this.q10Boolean = data[21];
                this.q10Comments = data[22];
                this.q11Boolean = data[23];
                this.q11Comments = data[24];
                this.q12Boolean = data[25];
                this.q12Comments = data[26];
                this.q13Boolean = data[27];
                this.q13Comments = data[28];
                this.q14Boolean = data[29];
                this.q14Comments = data[30];
                this.q15Boolean = data[31];
                this.q15Comments = data[32];
                this.q16Boolean = data[33];
                this.q16Comments = data[34];
                dataLoadedFromDatabase = true ;
            }
        }

        private Row( DBResults results )
        {
            this( results.getRow(0) );
        }

        public int getID()
        {
            return id ;
        }

        public void setID( int id )
        {
            this.id = id ;
        }


        public int getEntrypointID()
        {
            return entrypointID ;
        }

        public void setEntrypointID( int entrypointID )
        {
            this.entrypointID = entrypointID ;
        }


        public String getQ1Value()
        {
            return q1Value ;
        }

        public void setQ1Value( String q1Value )
        {
            this.q1Value = q1Value ;
        }


        public String getQ1Comments()
        {
            return q1Comments ;
        }

        public void setQ1Comments( String q1Comments )
        {
            this.q1Comments = q1Comments ;
        }


        public String getQ2Boolean()
        {
            return q2Boolean ;
        }

        public void setQ2Boolean( String q2Boolean )
        {
            this.q2Boolean = q2Boolean ;
        }


        public String getQ2Comments()
        {
            return q2Comments ;
        }

        public void setQ2Comments( String q2Comments )
        {
            this.q2Comments = q2Comments ;
        }


        public String getQ3Boolean()
        {
            return q3Boolean ;
        }

        public void setQ3Boolean( String q3Boolean )
        {
            this.q3Boolean = q3Boolean ;
        }


        public String getQ3Comments()
        {
            return q3Comments ;
        }

        public void setQ3Comments( String q3Comments )
        {
            this.q3Comments = q3Comments ;
        }


        public String getQ4Value()
        {
            return q4Value ;
        }

        public void setQ4Value( String q4Value )
        {
            this.q4Value = q4Value ;
        }


        public String getQ4Comments()
        {
            return q4Comments ;
        }

        public void setQ4Comments( String q4Comments )
        {
            this.q4Comments = q4Comments ;
        }


        public String getQ5DimsH()
        {
            return q5DimsH ;
        }

        public void setQ5DimsH( String q5DimsH )
        {
            this.q5DimsH = q5DimsH ;
        }


        public String getQ5DimsW()
        {
            return q5DimsW ;
        }

        public void setQ5DimsW( String q5DimsW )
        {
            this.q5DimsW = q5DimsW ;
        }


        public String getQ5Comments()
        {
            return q5Comments ;
        }

        public void setQ5Comments( String q5Comments )
        {
            this.q5Comments = q5Comments ;
        }


        public String getQ6Boolean()
        {
            return q6Boolean ;
        }

        public void setQ6Boolean( String q6Boolean )
        {
            this.q6Boolean = q6Boolean ;
        }


        public String getQ6Comments()
        {
            return q6Comments ;
        }

        public void setQ6Comments( String q6Comments )
        {
            this.q6Comments = q6Comments ;
        }


        public String getQ7Value()
        {
            return q7Value ;
        }

        public void setQ7Value( String q7Value )
        {
            this.q7Value = q7Value ;
        }


        public String getQ7Comments()
        {
            return q7Comments ;
        }

        public void setQ7Comments( String q7Comments )
        {
            this.q7Comments = q7Comments ;
        }


        public String getQ8Boolean()
        {
            return q8Boolean ;
        }

        public void setQ8Boolean( String q8Boolean )
        {
            this.q8Boolean = q8Boolean ;
        }


        public String getQ8Comments()
        {
            return q8Comments ;
        }

        public void setQ8Comments( String q8Comments )
        {
            this.q8Comments = q8Comments ;
        }


        public String getQ9Boolean()
        {
            return q9Boolean ;
        }

        public void setQ9Boolean( String q9Boolean )
        {
            this.q9Boolean = q9Boolean ;
        }


        public String getQ9Comments()
        {
            return q9Comments ;
        }

        public void setQ9Comments( String q9Comments )
        {
            this.q9Comments = q9Comments ;
        }


        public String getQ10Boolean()
        {
            return q10Boolean ;
        }

        public void setQ10Boolean( String q10Boolean )
        {
            this.q10Boolean = q10Boolean ;
        }


        public String getQ10Comments()
        {
            return q10Comments ;
        }

        public void setQ10Comments( String q10Comments )
        {
            this.q10Comments = q10Comments ;
        }


        public String getQ11Boolean()
        {
            return q11Boolean ;
        }

        public void setQ11Boolean( String q11Boolean )
        {
            this.q11Boolean = q11Boolean ;
        }


        public String getQ11Comments()
        {
            return q11Comments ;
        }

        public void setQ11Comments( String q11Comments )
        {
            this.q11Comments = q11Comments ;
        }


        public String getQ12Boolean()
        {
            return q12Boolean ;
        }

        public void setQ12Boolean( String q12Boolean )
        {
            this.q12Boolean = q12Boolean ;
        }


        public String getQ12Comments()
        {
            return q12Comments ;
        }

        public void setQ12Comments( String q12Comments )
        {
            this.q12Comments = q12Comments ;
        }


        public String getQ13Boolean()
        {
            return q13Boolean ;
        }

        public void setQ13Boolean( String q13Boolean )
        {
            this.q13Boolean = q13Boolean ;
        }


        public String getQ13Comments()
        {
            return q13Comments ;
        }

        public void setQ13Comments( String q13Comments )
        {
            this.q13Comments = q13Comments ;
        }


        public String getQ14Boolean()
        {
            return q14Boolean ;
        }

        public void setQ14Boolean( String q14Boolean )
        {
            this.q14Boolean = q14Boolean ;
        }


        public String getQ14Comments()
        {
            return q14Comments ;
        }

        public void setQ14Comments( String q14Comments )
        {
            this.q14Comments = q14Comments ;
        }


        public String getQ15Boolean()
        {
            return q15Boolean ;
        }

        public void setQ15Boolean( String q15Boolean )
        {
            this.q15Boolean = q15Boolean ;
        }


        public String getQ15Comments()
        {
            return q15Comments ;
        }

        public void setQ15Comments( String q15Comments )
        {
            this.q15Comments = q15Comments ;
        }


        public String getQ16Boolean()
        {
            return q16Boolean ;
        }

        public void setQ16Boolean( String q16Boolean )
        {
            this.q16Boolean = q16Boolean ;
        }


        public String getQ16Comments()
        {
            return q16Comments ;
        }

        public void setQ16Comments( String q16Comments )
        {
            this.q16Comments = q16Comments ;
        }



        
        private boolean dataLoadedFromDatabase()
        {
            return dataLoadedFromDatabase ;
        }

        private Map buildDataMap()
        {
            Map data = new HashMap();
            data.put( idColumnName , String.valueOf(  this.id ) );
            data.put( entrypointIDColumnName , String.valueOf(  this.entrypointID ) );
            data.put( q1ValueColumnName , this.q1Value );
            data.put( q1CommentsColumnName , this.q1Comments );
            data.put( q2BooleanColumnName , this.q2Boolean );
            data.put( q2CommentsColumnName , this.q2Comments );
            data.put( q3BooleanColumnName , this.q3Boolean );
            data.put( q3CommentsColumnName , this.q3Comments );
            data.put( q4ValueColumnName , this.q4Value );
            data.put( q4CommentsColumnName , this.q4Comments );
            data.put( q5DimsHColumnName , this.q5DimsH );
            data.put( q5DimsWColumnName , this.q5DimsW );
            data.put( q5CommentsColumnName , this.q5Comments );
            data.put( q6BooleanColumnName , this.q6Boolean );
            data.put( q6CommentsColumnName , this.q6Comments );
            data.put( q7ValueColumnName , this.q7Value );
            data.put( q7CommentsColumnName , this.q7Comments );
            data.put( q8BooleanColumnName , this.q8Boolean );
            data.put( q8CommentsColumnName , this.q8Comments );
            data.put( q9BooleanColumnName , this.q9Boolean );
            data.put( q9CommentsColumnName , this.q9Comments );
            data.put( q10BooleanColumnName , this.q10Boolean );
            data.put( q10CommentsColumnName , this.q10Comments );
            data.put( q11BooleanColumnName , this.q11Boolean );
            data.put( q11CommentsColumnName , this.q11Comments );
            data.put( q12BooleanColumnName , this.q12Boolean );
            data.put( q12CommentsColumnName , this.q12Comments );
            data.put( q13BooleanColumnName , this.q13Boolean );
            data.put( q13CommentsColumnName , this.q13Comments );
            data.put( q14BooleanColumnName , this.q14Boolean );
            data.put( q14CommentsColumnName , this.q14Comments );
            data.put( q15BooleanColumnName , this.q15Boolean );
            data.put( q15CommentsColumnName , this.q15Comments );
            data.put( q16BooleanColumnName , this.q16Boolean );
            data.put( q16CommentsColumnName , this.q16Comments );
            return data ;
        }

        /** update a row object based on a search */
        public void update( Connection con , String column , String searchText ) throws SQLException
        {
            imp.update( con , column , searchText , buildDataMap() );
        }

        /** update a row object based on a search */
        public void update( String column , String searchText ) throws SQLException
        {
            imp.update( column , searchText , buildDataMap() );
        }

        /** update a row object based on the id */
        public void update( Connection con ) throws SQLException
        {
            imp.update( con , id , buildDataMap() );
        }

        /** update a row object based on the id */
        public void update() throws SQLException
        {
            imp.update( id , buildDataMap() );
        }

        /** create a new row complete with a new ID.

            The current ID is ignored.  The new ID is placed in the row.

            @return the new row ID 
        */
        public long insert( Connection con ) throws SQLException
        {
            return imp.insertAndGetID( con , buildDataMap() );
        }

        /** create a new row complete with a new ID.

            The current ID is ignored.  The new ID is placed in the row.

            @return the new row ID 
        */
        public long insert() throws SQLException
        {
            return imp.insertAndGetID( buildDataMap() );
        }

        /** delete a row object based on the id */
        public void delete( Connection con ) throws SQLException
        {
            imp.delete( con , id );
        }

        /** delete a row object based on the id */
        public void delete() throws SQLException
        {
            imp.delete( id );
        }


    }

    /** Return an empty row object */
    public static Row getRow()
    {
        return imp.getRow();
    }

    /** Instantiate a Row object and fill its content based on a search for the ID. 
     *
     * Return null if not found.
     */
    public static Row getRow( Connection con , int id ) throws SQLException
    {
        return imp.getRow( con , id );
    }

    /** Instantiate a Row object and fill its content based on a search for the ID. 
     *
     * Return null if not found.
     */
    public static Row getRow( long id ) throws SQLException
    {
        return imp.getRow( id );
    }

    /** Instantiate a Row object and fill its content based on a search
     *
     * Return null if not found.
     */
    public static Row getRow( Connection con , String column , String searchText ) throws SQLException
    {
        return imp.getRow( con , column , searchText );
    }

    /** Instantiate a Row object and fill its content based on a search
     *
     * Return null if not found.
     */
    public static Row getRow( String column , String searchText ) throws SQLException
    {
        return imp.getRow( column , searchText );
    }

    /** Return an array of length zero if nothing found */
    public static Row[] getRows( Connection con , String column , String searchText ) throws SQLException
    {
        return imp.getRows( con , column , searchText );
    }

    /** Return an array of length zero if nothing found */
    public static Row[] getRows( String column , String searchText ) throws SQLException
    {
        return imp.getRows( column , searchText );
    }

    /** Return an array of length zero if nothing found */
    public static Row[] getRows( Connection con , String column , String[] searchText ) throws SQLException
    {
        return imp.getRows( con , column , searchText );
    }

    /** Return an array of length zero if nothing found */
    public static Row[] getRows( String column , String[] searchText ) throws SQLException
    {
        return imp.getRows( column , searchText );
    }

    /** Return an array of length zero if nothing found */
    public static Row[] getRows( Connection con , String column , int searchValue ) throws SQLException
    {
        return imp.getRows( con , column , String.valueOf( searchValue ) );
    }

    /** Return an array of length zero if nothing found */
    public static Row[] getRows( String column , int searchValue ) throws SQLException
    {
        return imp.getRows( column , String.valueOf( searchValue ) );
    }

    /** Return an array of length zero if nothing found */
    public static Row[] getRows( Connection con , String column , int[] searchValues ) throws SQLException
    {
        return imp.getRows( con , column , Str.toStringArray( searchValues ) );
    }

    /** Return an array of length zero if nothing found */
    public static Row[] getRows( String column , int[] searchValues ) throws SQLException
    {
        return imp.getRows( column , Str.toStringArray( searchValues ) );
    }

    /** Return an array of length zero if nothing found */
    public static Row[] getRows( Connection con , String whereClause ) throws SQLException
    {
        return imp.getRows( con , whereClause );
    }

    /** Return an array of length zero if nothing found */
    public static Row[] getRows( String whereClause ) throws SQLException
    {
        return imp.getRows( whereClause );
    }

    /** Return an array of length zero if nothing found */
    public static Row[] getAllRows( Connection con ) throws SQLException
    {
        return imp.getAllRows( con );
    }

    /** Return an array of length zero if nothing found */
    public static Row[] getAllRows() throws SQLException
    {
        return imp.getAllRows();
    }

    public static DBResults search( Connection con , String column , String searchText , String[] dataColumns ) throws SQLException
    {
        return imp.search( con , column , searchText , dataColumns );
    }

    public static DBResults search( String column , String searchText , String[] dataColumns ) throws SQLException
    {
        return imp.search( column , searchText , dataColumns );
    }

    public static DBResults search( Connection con , String column , String[] searchText , String[] dataColumns ) throws SQLException
    {
        return imp.search( con , column , searchText , dataColumns );
    }

    public static DBResults search( String column , String searchText[] , String[] dataColumns ) throws SQLException
    {
        return imp.search( column , searchText , dataColumns );
    }

    public static DBResults search( Connection con , String column , int searchValue , String[] dataColumns ) throws SQLException
    {
        return imp.search( con , column , searchValue , dataColumns );
    }

    public static DBResults search( String column , int searchValue , String[] dataColumns ) throws SQLException
    {
        return imp.search( column , searchValue , dataColumns );
    }

    public static DBResults search( Connection con , String column , int[] searchValues , String[] dataColumns ) throws SQLException
    {
        return imp.search( con , column , searchValues , dataColumns );
    }

    public static DBResults search( String column , int[] searchValues , String[] dataColumns ) throws SQLException
    {
        return imp.search( column , searchValues , dataColumns );
    }

    public static DBResults search( Connection con , String whereClause , String[] dataColumns ) throws SQLException
    {
        return imp.search( con , whereClause , dataColumns );
    }

    public static DBResults search( String whereClause , String[] dataColumns ) throws SQLException
    {
        return imp.search( whereClause , dataColumns );
    }

    public static DBResults search( Connection con , String[] dataColumns ) throws SQLException
    {
        return imp.search( con , dataColumns );
    }

    public static DBResults search( String[] dataColumns ) throws SQLException
    {
        return imp.search( dataColumns );
    }

    public static void update( Connection con , String column , String searchText , Map data ) throws SQLException
    {
        imp.update( con , column , searchText , data );
    }

    public static void update( String column , String searchText , Map data ) throws SQLException
    {
        imp.update( column , searchText , data );
    }

    public static void delete( Connection con , long id ) throws SQLException
    {
        imp.delete( con , id );
    }

    public static void delete( long id ) throws SQLException
    {
        imp.delete( id );
    }

    public static void delete( Connection con , String column , String searchText ) throws SQLException
    {
        imp.delete( con , column , searchText );
    }

    public static void delete( String column , String searchText ) throws SQLException
    {
        imp.delete( column , searchText );
    }

    public static long insert( Connection con , Map data ) throws SQLException
    {
        return imp.insertAndGetID( con , data );
    }

    public static long insert( Map data ) throws SQLException
    {
        return imp.insertAndGetID( data );
    }



}

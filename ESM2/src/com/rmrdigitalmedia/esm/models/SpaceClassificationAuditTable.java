package com.rmrdigitalmedia.esm.models ;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.javaranch.common.Str;
import com.javaranch.db.DBResults;
import com.javaranch.db.TableFacade;

/** Strongly typed access to the database table "SPACE_CLASSIFICATION_AUDIT".
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
public class SpaceClassificationAuditTable
{

    private static Implementation imp = new Implementation();

    public static final String tableName = "SPACE_CLASSIFICATION_AUDIT";

    public static final String idColumnName = "ID";
    public static final String spaceIDColumnName = "SPACE_ID";
    public static final String q1ValueColumnName = "Q1_VALUE";
    public static final String q1CommentsColumnName = "Q1_COMMENTS";
    public static final String q2ValueColumnName = "Q2_VALUE";
    public static final String q2CommentsColumnName = "Q2_COMMENTS";
    public static final String q3ValueColumnName = "Q3_VALUE";
    public static final String q3CommentsColumnName = "Q3_COMMENTS";
    public static final String q4ValueColumnName = "Q4_VALUE";
    public static final String q4CommentsColumnName = "Q4_COMMENTS";
    public static final String q5ValueColumnName = "Q5_VALUE";
    public static final String q5CommentsColumnName = "Q5_COMMENTS";
    public static final String q6ValueColumnName = "Q6_VALUE";
    public static final String q6CommentsColumnName = "Q6_COMMENTS";
    public static final String q7BooleanColumnName = "Q7_BOOLEAN";
    public static final String q7CommentsColumnName = "Q7_COMMENTS";
    public static final String q8BooleanColumnName = "Q8_BOOLEAN";
    public static final String q8CommentsColumnName = "Q8_COMMENTS";
    public static final String remoteIdentifierColumnName = "REMOTE_IDENTIFIER";

    private static String[] allColumns =
    {
        idColumnName , spaceIDColumnName , q1ValueColumnName , q1CommentsColumnName , q2ValueColumnName , q2CommentsColumnName , q3ValueColumnName , q3CommentsColumnName , q4ValueColumnName , q4CommentsColumnName , q5ValueColumnName , q5CommentsColumnName , q6ValueColumnName , q6CommentsColumnName , q7BooleanColumnName , q7CommentsColumnName , q8BooleanColumnName , q8CommentsColumnName , remoteIdentifierColumnName , 
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
     *  To use this in your unit testing, create an instance of MockSpaceClassificationAuditTable and pass
     *  it in here.  Then set your mock return values, call the method you are testing and examine
     *  the mock values that are now set!
     */
    public static void setInstance( SpaceClassificationAuditTable.Implementation instance  )
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
        private int spaceID ;
        private int q1Value ;
        private boolean q1ValueNull = true ;
        private String q1Comments ;
        private int q2Value ;
        private boolean q2ValueNull = true ;
        private String q2Comments ;
        private int q3Value ;
        private boolean q3ValueNull = true ;
        private String q3Comments ;
        private int q4Value ;
        private boolean q4ValueNull = true ;
        private String q4Comments ;
        private int q5Value ;
        private boolean q5ValueNull = true ;
        private String q5Comments ;
        private int q6Value ;
        private boolean q6ValueNull = true ;
        private String q6Comments ;
        private String q7Boolean ;
        private String q7Comments ;
        private String q8Boolean ;
        private String q8Comments ;
        private String remoteIdentifier ;

        /** for internal use only!   If you need a row object, use getRow(). */
        Row()
        {
        }

        private Row( String[] data )
        {
            if ( data != null )
            {
                this.id =  Str.toInt( data[0] );
                this.spaceID =  Str.toInt( data[1] );
                this.q1ValueNull = ( data[2] == null );
                this.q1Value = q1ValueNull ? 0 : Str.toInt( data[2] );
                this.q1Comments = data[3];
                this.q2ValueNull = ( data[4] == null );
                this.q2Value = q2ValueNull ? 0 : Str.toInt( data[4] );
                this.q2Comments = data[5];
                this.q3ValueNull = ( data[6] == null );
                this.q3Value = q3ValueNull ? 0 : Str.toInt( data[6] );
                this.q3Comments = data[7];
                this.q4ValueNull = ( data[8] == null );
                this.q4Value = q4ValueNull ? 0 : Str.toInt( data[8] );
                this.q4Comments = data[9];
                this.q5ValueNull = ( data[10] == null );
                this.q5Value = q5ValueNull ? 0 : Str.toInt( data[10] );
                this.q5Comments = data[11];
                this.q6ValueNull = ( data[12] == null );
                this.q6Value = q6ValueNull ? 0 : Str.toInt( data[12] );
                this.q6Comments = data[13];
                this.q7Boolean = data[14];
                this.q7Comments = data[15];
                this.q8Boolean = data[16];
                this.q8Comments = data[17];
                this.remoteIdentifier = data[18];
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


        public int getSpaceID()
        {
            return spaceID ;
        }

        public void setSpaceID( int spaceID )
        {
            this.spaceID = spaceID ;
        }


        public int getQ1Value()
        {
            return q1Value ;
        }

        public void setQ1Value( int q1Value )
        {
            this.q1Value = q1Value ;
            q1ValueNull = false ;
        }

        public void setQ1Value( Integer q1Value )
        {
            q1ValueNull = ( q1Value == null );
            if ( q1ValueNull )
            {
                this.q1Value = 0 ;
            }
            else
            {
                this.q1Value = q1Value.intValue() ;
            }
        }

        public boolean isQ1ValueNull()
        {
            return q1ValueNull ;
        }

        public void setQ1ValueNull( boolean q1ValueNull )
        {
            this.q1ValueNull = q1ValueNull ;
            if ( q1ValueNull )
            {
                q1Value = 0 ;
            }
        }


        public String getQ1Comments()
        {
            return q1Comments ;
        }

        public void setQ1Comments( String q1Comments )
        {
            this.q1Comments = q1Comments ;
        }


        public int getQ2Value()
        {
            return q2Value ;
        }

        public void setQ2Value( int q2Value )
        {
            this.q2Value = q2Value ;
            q2ValueNull = false ;
        }

        public void setQ2Value( Integer q2Value )
        {
            q2ValueNull = ( q2Value == null );
            if ( q2ValueNull )
            {
                this.q2Value = 0 ;
            }
            else
            {
                this.q2Value = q2Value.intValue() ;
            }
        }

        public boolean isQ2ValueNull()
        {
            return q2ValueNull ;
        }

        public void setQ2ValueNull( boolean q2ValueNull )
        {
            this.q2ValueNull = q2ValueNull ;
            if ( q2ValueNull )
            {
                q2Value = 0 ;
            }
        }


        public String getQ2Comments()
        {
            return q2Comments ;
        }

        public void setQ2Comments( String q2Comments )
        {
            this.q2Comments = q2Comments ;
        }


        public int getQ3Value()
        {
            return q3Value ;
        }

        public void setQ3Value( int q3Value )
        {
            this.q3Value = q3Value ;
            q3ValueNull = false ;
        }

        public void setQ3Value( Integer q3Value )
        {
            q3ValueNull = ( q3Value == null );
            if ( q3ValueNull )
            {
                this.q3Value = 0 ;
            }
            else
            {
                this.q3Value = q3Value.intValue() ;
            }
        }

        public boolean isQ3ValueNull()
        {
            return q3ValueNull ;
        }

        public void setQ3ValueNull( boolean q3ValueNull )
        {
            this.q3ValueNull = q3ValueNull ;
            if ( q3ValueNull )
            {
                q3Value = 0 ;
            }
        }


        public String getQ3Comments()
        {
            return q3Comments ;
        }

        public void setQ3Comments( String q3Comments )
        {
            this.q3Comments = q3Comments ;
        }


        public int getQ4Value()
        {
            return q4Value ;
        }

        public void setQ4Value( int q4Value )
        {
            this.q4Value = q4Value ;
            q4ValueNull = false ;
        }

        public void setQ4Value( Integer q4Value )
        {
            q4ValueNull = ( q4Value == null );
            if ( q4ValueNull )
            {
                this.q4Value = 0 ;
            }
            else
            {
                this.q4Value = q4Value.intValue() ;
            }
        }

        public boolean isQ4ValueNull()
        {
            return q4ValueNull ;
        }

        public void setQ4ValueNull( boolean q4ValueNull )
        {
            this.q4ValueNull = q4ValueNull ;
            if ( q4ValueNull )
            {
                q4Value = 0 ;
            }
        }


        public String getQ4Comments()
        {
            return q4Comments ;
        }

        public void setQ4Comments( String q4Comments )
        {
            this.q4Comments = q4Comments ;
        }


        public int getQ5Value()
        {
            return q5Value ;
        }

        public void setQ5Value( int q5Value )
        {
            this.q5Value = q5Value ;
            q5ValueNull = false ;
        }

        public void setQ5Value( Integer q5Value )
        {
            q5ValueNull = ( q5Value == null );
            if ( q5ValueNull )
            {
                this.q5Value = 0 ;
            }
            else
            {
                this.q5Value = q5Value.intValue() ;
            }
        }

        public boolean isQ5ValueNull()
        {
            return q5ValueNull ;
        }

        public void setQ5ValueNull( boolean q5ValueNull )
        {
            this.q5ValueNull = q5ValueNull ;
            if ( q5ValueNull )
            {
                q5Value = 0 ;
            }
        }


        public String getQ5Comments()
        {
            return q5Comments ;
        }

        public void setQ5Comments( String q5Comments )
        {
            this.q5Comments = q5Comments ;
        }


        public int getQ6Value()
        {
            return q6Value ;
        }

        public void setQ6Value( int q6Value )
        {
            this.q6Value = q6Value ;
            q6ValueNull = false ;
        }

        public void setQ6Value( Integer q6Value )
        {
            q6ValueNull = ( q6Value == null );
            if ( q6ValueNull )
            {
                this.q6Value = 0 ;
            }
            else
            {
                this.q6Value = q6Value.intValue() ;
            }
        }

        public boolean isQ6ValueNull()
        {
            return q6ValueNull ;
        }

        public void setQ6ValueNull( boolean q6ValueNull )
        {
            this.q6ValueNull = q6ValueNull ;
            if ( q6ValueNull )
            {
                q6Value = 0 ;
            }
        }


        public String getQ6Comments()
        {
            return q6Comments ;
        }

        public void setQ6Comments( String q6Comments )
        {
            this.q6Comments = q6Comments ;
        }


        public String getQ7Boolean()
        {
            return q7Boolean ;
        }

        public void setQ7Boolean( String q7Boolean )
        {
            this.q7Boolean = q7Boolean ;
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


        public String getRemoteIdentifier()
        {
            return remoteIdentifier ;
        }

        public void setRemoteIdentifier( String remoteIdentifier )
        {
            this.remoteIdentifier = remoteIdentifier ;
        }



        
        private boolean dataLoadedFromDatabase()
        {
            return dataLoadedFromDatabase ;
        }

        private Map buildDataMap()
        {
            Map data = new HashMap();
            data.put( idColumnName , String.valueOf(  this.id ) );
            data.put( spaceIDColumnName , String.valueOf(  this.spaceID ) );
            data.put( q1ValueColumnName , this.q1ValueNull ? null : String.valueOf( this.q1Value ) );
            data.put( q1CommentsColumnName , this.q1Comments );
            data.put( q2ValueColumnName , this.q2ValueNull ? null : String.valueOf( this.q2Value ) );
            data.put( q2CommentsColumnName , this.q2Comments );
            data.put( q3ValueColumnName , this.q3ValueNull ? null : String.valueOf( this.q3Value ) );
            data.put( q3CommentsColumnName , this.q3Comments );
            data.put( q4ValueColumnName , this.q4ValueNull ? null : String.valueOf( this.q4Value ) );
            data.put( q4CommentsColumnName , this.q4Comments );
            data.put( q5ValueColumnName , this.q5ValueNull ? null : String.valueOf( this.q5Value ) );
            data.put( q5CommentsColumnName , this.q5Comments );
            data.put( q6ValueColumnName , this.q6ValueNull ? null : String.valueOf( this.q6Value ) );
            data.put( q6CommentsColumnName , this.q6Comments );
            data.put( q7BooleanColumnName , this.q7Boolean );
            data.put( q7CommentsColumnName , this.q7Comments );
            data.put( q8BooleanColumnName , this.q8Boolean );
            data.put( q8CommentsColumnName , this.q8Comments );
            data.put( remoteIdentifierColumnName , this.remoteIdentifier );
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

package com.rmrdigitalmedia.esm.models ;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import com.javaranch.common.Str;
import com.javaranch.db.DBResults;
import com.javaranch.db.TableFacade;

/** Strongly typed access to the database table "ESM_USERS".
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
public class EsmUsersTable
{

    private static Implementation imp = new Implementation();

    public static final String tableName = "ESM_USERS";

    public static final String idColumnName = "ID";
    public static final String usernameColumnName = "USERNAME";
    public static final String passwordColumnName = "PASSWORD";
    public static final String forenameColumnName = "FORENAME";
    public static final String surnameColumnName = "SURNAME";
    public static final String rankColumnName = "RANK";
    public static final String jobTitleColumnName = "JOB_TITLE";
    public static final String workIdentifierColumnName = "WORK_IDENTIFIER";
    public static final String accessLevelColumnName = "ACCESS_LEVEL";
    public static final String dobColumnName = "DOB";
    public static final String commentColumnName = "COMMENT";
    public static final String createdDateColumnName = "CREATED_DATE";
    public static final String updateDateColumnName = "UPDATE_DATE";
    public static final String deletedColumnName = "DELETED";

    private static String[] allColumns =
    {
        idColumnName , usernameColumnName , passwordColumnName , forenameColumnName , surnameColumnName , rankColumnName , jobTitleColumnName , workIdentifierColumnName , accessLevelColumnName , dobColumnName , commentColumnName , createdDateColumnName , updateDateColumnName , deletedColumnName , 
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
     *  To use this in your unit testing, create an instance of MockEsmUsersTable and pass
     *  it in here.  Then set your mock return values, call the method you are testing and examine
     *  the mock values that are now set!
     */
    public static void setInstance( EsmUsersTable.Implementation instance  )
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
        private String username ;
        private String password ;
        private String forename ;
        private String surname ;
        private String rank ;
        private String jobTitle ;
        private String workIdentifier ;
        private int accessLevel ;
        private boolean accessLevelNull = true ;
        private String dob ;
        private String comment ;
        private Timestamp createdDate ;
        private Timestamp updateDate ;
        private String deleted ;

        /** for internal use only!   If you need a row object, use getRow(). */
        Row()
        {
        }

        private Row( String[] data )
        {
            if ( data != null )
            {
                this.id =  Str.toInt( data[0] );
                this.username = data[1];
                this.password = data[2];
                this.forename = data[3];
                this.surname = data[4];
                this.rank = data[5];
                this.jobTitle = data[6];
                this.workIdentifier = data[7];
                this.accessLevelNull = ( data[8] == null );
                this.accessLevel = accessLevelNull ? 0 : Str.toInt( data[8] );
                this.dob = data[9];
                this.comment = data[10];
                this.createdDate = Str.toTimestamp( data[11] );
                this.updateDate = Str.toTimestamp( data[12] );
                this.deleted = data[13];
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


        public String getUsername()
        {
            return username ;
        }

        public void setUsername( String username )
        {
            this.username = username ;
        }


        public String getPassword()
        {
            return password ;
        }

        public void setPassword( String password )
        {
            this.password = password ;
        }


        public String getForename()
        {
            return forename ;
        }

        public void setForename( String forename )
        {
            this.forename = forename ;
        }


        public String getSurname()
        {
            return surname ;
        }

        public void setSurname( String surname )
        {
            this.surname = surname ;
        }


        public String getRank()
        {
            return rank ;
        }

        public void setRank( String rank )
        {
            this.rank = rank ;
        }


        public String getJobTitle()
        {
            return jobTitle ;
        }

        public void setJobTitle( String jobTitle )
        {
            this.jobTitle = jobTitle ;
        }


        public String getWorkIdentifier()
        {
            return workIdentifier ;
        }

        public void setWorkIdentifier( String workIdentifier )
        {
            this.workIdentifier = workIdentifier ;
        }


        public int getAccessLevel()
        {
            return accessLevel ;
        }

        public void setAccessLevel( int accessLevel )
        {
            this.accessLevel = accessLevel ;
            accessLevelNull = false ;
        }

        public void setAccessLevel( Integer accessLevel )
        {
            accessLevelNull = ( accessLevel == null );
            if ( accessLevelNull )
            {
                this.accessLevel = 0 ;
            }
            else
            {
                this.accessLevel = accessLevel.intValue() ;
            }
        }

        public boolean isAccessLevelNull()
        {
            return accessLevelNull ;
        }

        public void setAccessLevelNull( boolean accessLevelNull )
        {
            this.accessLevelNull = accessLevelNull ;
            if ( accessLevelNull )
            {
                accessLevel = 0 ;
            }
        }


        public String getDob()
        {
            return dob ;
        }

        public void setDob( String dob )
        {
            this.dob = dob ;
        }


        public String getComment()
        {
            return comment ;
        }

        public void setComment( String comment )
        {
            this.comment = comment ;
        }


        public Timestamp getCreatedDate()
        {
            return createdDate ;
        }

        public void setCreatedDate( Timestamp createdDate )
        {
            this.createdDate = createdDate ;
        }


        public Timestamp getUpdateDate()
        {
            return updateDate ;
        }

        public void setUpdateDate( Timestamp updateDate )
        {
            this.updateDate = updateDate ;
        }


        public String getDeleted()
        {
            return deleted ;
        }

        public void setDeleted( String deleted )
        {
            this.deleted = deleted ;
        }



        
        private boolean dataLoadedFromDatabase()
        {
            return dataLoadedFromDatabase ;
        }

        private Map buildDataMap()
        {
            Map data = new HashMap();
            data.put( idColumnName , String.valueOf(  this.id ) );
            data.put( usernameColumnName , this.username );
            data.put( passwordColumnName , this.password );
            data.put( forenameColumnName , this.forename );
            data.put( surnameColumnName , this.surname );
            data.put( rankColumnName , this.rank );
            data.put( jobTitleColumnName , this.jobTitle );
            data.put( workIdentifierColumnName , this.workIdentifier );
            data.put( accessLevelColumnName , this.accessLevelNull ? null : String.valueOf( this.accessLevel ) );
            data.put( dobColumnName , this.dob );
            data.put( commentColumnName , this.comment );
            data.put( createdDateColumnName , this.createdDate == null ? null : this.createdDate.toString() );
            data.put( updateDateColumnName , this.updateDate == null ? null : this.updateDate.toString() );
            data.put( deletedColumnName , this.deleted );
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

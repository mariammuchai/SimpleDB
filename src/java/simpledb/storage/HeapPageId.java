package simpledb.storage;

import java.util.Objects;

/**
 * Unique identifier for HeapPage objects.
 */
public class HeapPageId implements PageId {

    /**
     * Constructor. Create a page id structure for a specific page of a
     * specific table.
     *
     * @param tableId The table that is being referenced
     * @param pgNo    The page number in that table.
     */
    private int tableId;
    private int pgNo;
    public HeapPageId(int tableId, int pgNo) {
        // implementation of constructor
        this.tableId = tableId;
        this.pgNo = pgNo;
    }

    /**
     * @return the table associated with this PageId
     */
    public int getTableId() {
        // returns the value stored in tableId
        return tableId;
    }

    /**
     * @return the page number in the table getTableId() associated with
     *         this PageId
     */
    public int getPageNumber() {
        // returns the page number
        return pgNo;
    }

    /**
     * @return a hash code for this page, represented by a combination of
     *         the table number and the page number (needed if a PageId is used as a
     *         key in a hash table in the BufferPool, for example.)
     * @see BufferPool
     */
    public int hashCode() {
        //done
        int result = 17;
        result = 31 * result + tableId;
        result = 31 * result + pgNo;
        return result;
    }

    /**
     * Compares one PageId to another.
     *
     * @param o The object to compare against (must be a PageId)
     * @return true if the objects are equal (e.g., page numbers and table
     *         ids are the same)
     */
    public boolean equals(Object o) {
        if (!(o instanceof HeapPageId)) {
            return false;
        }
        HeapPageId object2 = (HeapPageId) o;
        return this.getTableId() == object2.getTableId() && this.getPageNumber() == object2.getPageNumber();

    }

    /**
     * Return a representation of this object as an array of
     * integers, for writing to disk.  Size of returned array must contain
     * number of integers that corresponds to number of args to one of the
     * constructors.
     */
    public int[] serialize() {
        int[] data = new int[2];

        data[0] = getTableId();
        data[1] = getPageNumber();

        return data;
    }

}

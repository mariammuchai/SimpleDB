package simpledb.storage;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Tuple maintains information about the contents of a tuple. Tuples have a
 * specified schema specified by a TupleDesc object and contain Field objects
 * with the data for each field.
 */
public class Tuple implements Serializable {

    private static final long serialVersionUID = 1L; //identifies the class

    /**
     * Create a new tuple with the specified schema (type).
     *
     * @param td the schema of this tuple. It must be a valid TupleDesc
     *           instance with at least one field.
     */
    //initialize private variables
    private TupleDesc tupleDesc;
    private Field[] fields;
    private RecordId recordId;

    public Tuple(TupleDesc td) {
        //initialize private variables
        this.tupleDesc = td;
        this.fields = new Field[td.numFields()];
        this.recordId = null;

        //initialize fields with nulls
        for (int i = 0; i < this.fields.length; i++) {
            this.fields[i] = null;
        }

    }

    /**
     * @return The TupleDesc representing the schema of this tuple.
     */
    public TupleDesc getTupleDesc() {
        // returns value of instance variable 'tupleDesc'
        return this.tupleDesc;
    }

    /**
     * @return The RecordId representing the location of this tuple on disk. May
     *         be null.
     */
    public RecordId getRecordId() {
        // returns value of instance variable 'recordId'
        return this.recordId;
    }

    /**
     * Set the RecordId information for this tuple.
     *
     * @param rid the new RecordId for this tuple.
     */
    public void setRecordId(RecordId rid) {
        //set the value of the instance variable 'recordId'
        this.recordId = rid;
    }

    /**
     * Change the value of the ith field of this tuple.
     *
     * @param i index of the field to change. It must be a valid index.
     * @param f new value for the field.
     */
    public void setField(int i, Field f) {
        //change the value of a field in the tuple object, index should be valid
        if (!(i < 0 || i >= this.fields.length)) {
            this.fields[i] = f;
        }

    }

    /**
     * @param i field index to return. Must be a valid index.
     * @return the value of the ith field, or null if it has not been set.
     */
    public Field getField(int i) {
        // returns value of a field in the tuple, index should be valid
        if (!(i < 0 || i >= this.fields.length)) {
            return this.fields[i];
        }
        return this.fields[i];
    }

    /**
     * Returns the contents of this Tuple as a string. Note that to pass the
     * system tests, the format needs to be as follows:
     * <p>
     * column1\tcolumn2\tcolumn3\t...\tcolumnN
     * <p>
     * where \t is any whitespace (except a newline)
     */
    public String toString() {
        // TODO: some code goes here
        //throw new UnsupportedOperationException("Implement this");
        String tupleContent = "";
        for (int i = 0; i < this.fields.length; i++) {
            tupleContent += this.fields[i].toString() + "\t";

        }
        return  tupleContent;
    }

    /**
     * @return An iterator which iterates over all the fields of this tuple
     */
    public Iterator<Field> fields() {
        // done
        return Arrays.stream(fields).iterator();
    }

    /**
     * reset the TupleDesc of this tuple (only affecting the TupleDesc)
     */
    public void resetTupleDesc(TupleDesc td) {
        // done
        this.tupleDesc = td;
    }
}

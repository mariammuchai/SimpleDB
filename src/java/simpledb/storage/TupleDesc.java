package simpledb.storage;

import simpledb.common.Type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

    /**
     * A help class to facilitate organizing the information of each field
     */
    private TDItem[] items;
    public static class TDItem implements Serializable {


        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         */
        public final Type fieldType;

        /**
         * The name of the field
         */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return An iterator which iterates over all the field TDItems
     *         that are included in this TupleDesc
     */
    public Iterator<TDItem> iterator() {
        // done
        return Arrays.asList(items).iterator();
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     *
     * @param typeAr  array specifying the number of and types of fields in this
     *                TupleDesc. It must contain at least one entry.
     * @param fieldAr array specifying the names of the fields. Note that names may
     *                be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        //done
        items = new TDItem[typeAr.length];
        for (int i = 0; i < typeAr.length; i++) {
            items[i] = new TDItem(typeAr[i], fieldAr[i]);
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     *
     * @param typeAr array specifying the number of and types of fields in this
     *               TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // initialize items array
        items = new TDItem[typeAr.length];
        //create a TDItem object....field name not given, thus null
        for (int i = 0; i < typeAr.length; i++) {
            items[i] = new TDItem(typeAr[i], null);
        }
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // length of the items array
        return items.length;
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     *
     * @param i index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // if not a valid index throw an error
        if (i < 0 || i >= items.length) {
            throw new NoSuchElementException();
        }
        return items[i].fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     *
     * @param i The index of the field to get the type of. It must be a valid
     *          index.
     * @return the type of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        // if not a valid index throw an error
        if (i < 0 || i >= items.length) {
            throw new NoSuchElementException();
        }
        return items[i].fieldType;

    }

    /**
     * Find the index of the field with a given name.
     *
     * @param name name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException if no field with a matching name is found.
     */
    public int indexForFieldName(String name) throws NoSuchElementException {
        // looping over items array
        for (int i = 0; i < items.length; i++) {
            if (name.equals(items[i].fieldName)) {
                return i;
            }
        }
        throw new NoSuchElementException();

    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // done
        int size = 0;
        for (TDItem item: items) {
            size += item.fieldType.getLen();
        }
        return size;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     *
     * @param td1 The TupleDesc with the first fields of the new TupleDesc
     * @param td2 The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        int fieldCounts = td1.numFields() + td2.numFields();
        Type[] types = new Type[fieldCounts];
        String[] names = new String[fieldCounts];

        for (int i = 0; i < td1.numFields(); i++) {
            types[i] = td1.getFieldType(i);
            names[i] = td1.getFieldName(i);
        }
        for (int i = 0; i < td2.numFields(); i++) {
            types[i + td1.numFields()] = td2.getFieldType(i);
            names[i + td1.numFields()] = td2.getFieldName(i);
        }
        return new TupleDesc(types, names);

    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they have the same number of items
     * and if the i-th type in this TupleDesc is equal to the i-th type in o
     * for every i.
     *
     * @param o the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */

    public boolean equals(Object o) {
        // check if object o is equal to this TupleDesc
        if (o == this) {
            return true;
        }
        //checking if o is an instance of the class TupleDesc
        if (!(o instanceof TupleDesc)) {
            return false;
        }
        //compare the number of fields in this TupleDesc and o
        TupleDesc object2 = (TupleDesc) o;
        if (this.numFields() != object2.numFields()) {
            return false;
        }
        //comparing the types of the fields in this TupleDesc and o
        for (int i = 0; i < this.numFields(); i++) {
            if (this.getFieldType(i) != object2.getFieldType(i)) {
                return false;
            }
        }
        return true;

    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     *
     * @return String describing this descriptor.
     */
    public String toString() {
        // ;
        String TupleDcontent = "";
        for (int i = 0; i < this.numFields(); i++) {
            TupleDcontent += getFieldType(i) + "[" + getFieldName(i) + "]";
        }
        return TupleDcontent;
    }
}

package simpledb.storage;

import simpledb.common.Database;
import simpledb.common.DbException;
import simpledb.common.Debug;
import simpledb.common.Permissions;
import simpledb.transaction.TransactionAbortedException;
import simpledb.transaction.TransactionId;
import simpledb.storage.PageId;
import simpledb.storage.BufferPool;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples
 * in no particular order. Tuples are stored on pages, each of which is a fixed
 * size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage
 * constructor.
 *
 * @author Sam Madden
 * @see HeapPage#HeapPage
 */
public class HeapFile implements DbFile {

    /**
     * Constructs a heap file backed by the specified file.
     *
     * @param f the file that stores the on-disk backing store for this heap
     *          file.
     */
    private File file;
    private TupleDesc tupleDesc;
    private PageId pid;
    private BufferPool bf;
    public HeapFile(File f, TupleDesc td) {
        // constructor
        this.file = f;
        this.tupleDesc = td;
    }

    /**
     * Returns the File backing this HeapFile on disk.
     *
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        // done
        return this.file;
    }

    /**
     * Returns an ID uniquely identifying this HeapFile. Implementation note:
     * you will need to generate this tableid somewhere to ensure that each
     * HeapFile has a "unique id," and that you always return the same value for
     * a particular HeapFile. We suggest hashing the absolute file name of the
     * file underlying the heapfile, i.e. f.getAbsoluteFile().hashCode().
     *
     * @return an ID uniquely identifying this HeapFile.
     */
    public int getId() {
        // done
        return file.getAbsoluteFile().hashCode();
    }

    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     *
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
        // done
        return this.tupleDesc;
    }

    // see DbFile.java for javadocs
    //pid....pageId of the page to return
    public Page readPage(PageId pid) {
        // calculate the offset in the file where the desired page is stored
        int offset = pid.getPageNumber() * Database.getBufferPool().getPageSize();
        //Allocate a buffer to store the contents of the page
        byte[] data = new byte[Database.getBufferPool().getPageSize()];
        try {
            //Open a random access file to read the page from disk
            RandomAccessFile file = new RandomAccessFile(getFile(), "r");
            //Move the file pointer to the desired offset
            file.seek(offset);
            //Read the page from disk into the buffer
            file.read(data, 0, Database.getBufferPool().getPageSize());
            //close the random access file
            file.close();
            //Create a new HeapPage using the data in the buffer
            return new HeapPage((HeapPageId) pid, data);
        } catch (IOException e) {
            //An error occurred while reading the page from disk
            e.printStackTrace();
            return null;
        }
    }

    // see DbFile.java for javadocs
    public void writePage(Page page) throws IOException {
        // TODO: some code goes here
        // not necessary for lab1
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    public int numPages() {
        // done;
        return (int)Math.ceil((double)file.length() / BufferPool.getPageSize());
    }

    // see DbFile.java for javadocs
    public List<Page> insertTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException {
        // TODO: some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public List<Page> deleteTuple(TransactionId tid, Tuple t) throws DbException,
            TransactionAbortedException {
        // TODO: some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public DbFileIterator iterator(TransactionId tid) {
        List<Page> pages = new ArrayList<>();
        BufferPool bf = Database.getBufferPool();
        for (int i = 0; i < numPages(); i++) {
            PageId pid = new HeapPageId(getId(), i);
            try {
                Page page = bf.getPage(tid, pid, Permissions.READ_ONLY);
                pages.add(page);
            } catch (TransactionAbortedException | DbException e) {
                e.printStackTrace();
            }
        }
        return new PageIteratorWrapper(pages.iterator());
    }

    class PageIteratorWrapper implements DbFileIterator {
        Iterator<Page> pages;
        int currentPage = 0;
        Page currentPageData;

        public PageIteratorWrapper(Iterator<Page> pages) {
            this.pages = pages;
        }

        public void open()
                throws DbException, TransactionAbortedException {
            // code to open the iterator
        }

        @Override
        public boolean hasNext() throws DbException, TransactionAbortedException {
            if (currentPageData == null || currentPage >= currentPageData.getNumTuples()) {
                // Check if there are more pages in the iterator
                if (!pages.hasNext()) {
                    return false;
                }

                // If there are, get the next page
                currentPageData = pages.next();
                currentPage = 0;
            }

            return true;
        }

        @Override
        public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
            if (hasNext()) {
                return currentPageData.getTuple(currentPage++);
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void rewind() throws DbException, TransactionAbortedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() {
            pages = null;
            currentPageData = null;
            currentPage = 0;
        }
    }


}







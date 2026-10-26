
import java.io.IOException;
import java.io.RandomAccessFile;

public class LibraryDB {

    // Fixed field lengths
    public static final int IDLEN = 12;
    public static final int TITLEN = 40;
    public static final int QUANLEN = 4;
    public static final int COSTLEN = 4;

    // Size of one record
    private static final int RECLEN
            = 2 * IDLEN + 2 * TITLEN + QUANLEN + COSTLEN;

    private RandomAccessFile raf;

    // Constructor
    public LibraryDB(String path) throws IOException {
        raf = new RandomAccessFile(path, "rw");
    }

    // Add a record at the end of the file
    public void append(String booknum, String title,
            int qty, int ucost) throws IOException {
                if (booknum.length() < IDLEN) {
                    for (int i = IDLEN - booknum.length(); i < IDLEN; i++) {
                        booknum += " ";
                    }
                } else if (booknum.length() > IDLEN) {
                    booknum = booknum.substring(0, IDLEN);
                }
                if (title.length() < TITLEN) {
                    for (int i = TITLEN - title.length(); i < TITLEN; i++) {
                        title += " ";
                    }
                } else if (title.length() > TITLEN) {
                    title = title.substring(0, TITLEN);
                }
        raf.seek(raf.length());

        write(booknum, title, qty, ucost);
    
            }
    // Close the database
    public void close() {
        try {
            raf.close();
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    // Return the number of records
    public int numRecs() throws IOException {
        return (int) (raf.length() / RECLEN);
    }

    // Read a record
    public Book select(int recno) throws IOException {

        if (recno < 0 || recno >= numRecs()) {
            throw new IllegalArgumentException(
                    recno + " out of range");
        }

        raf.seek(recno * RECLEN);

        return read();
    }

    // Update an existing record
    public void update(int recno, String booknum,
            String title, int qty,
            int ucost) throws IOException {

        if (recno < 0 || recno >= numRecs()) {
            throw new IllegalArgumentException(
                    recno + " out of range");
        }

        raf.seek(recno * RECLEN);

        write(booknum, title, qty, ucost);
    }

    // Read one record from the current file position
    private Book read() throws IOException {

        StringBuilder sb = new StringBuilder();

        // Read part number
        for (int i = 0; i < IDLEN; i++) {
            sb.append(raf.readChar());
        }

        String booknum = sb.toString().trim();

        // Read description
        sb.setLength(0);

        for (int i = 0; i < TITLEN; i++) {
            sb.append(raf.readChar());
        }

        String title = sb.toString().trim();

        // Read quantity and unit cost
        int qty = raf.readInt();
        int ucost = raf.readInt();

        return new Book(booknum, title, qty, ucost);
    }

    // Write one record
    private void write(String booknum, String title,
            int qty, int ucost) throws IOException {

        // Prepare part number
        StringBuilder sb = new StringBuilder(booknum);

        if (sb.length() > IDLEN) {
            sb.setLength(IDLEN);
        } else {
            while (sb.length() < IDLEN) {
                sb.append(" ");
            }
        }

        raf.writeChars(sb.toString());

        // Prepare description
        sb = new StringBuilder(title);

        if (sb.length() > TITLEN) {
            sb.setLength(TITLEN);
        } else {
            while (sb.length() < TITLEN) {
                sb.append(" ");
            }
        }

        raf.writeChars(sb.toString());

        // Write numeric fields
        raf.writeInt(qty);
        raf.writeInt(ucost);
    }

    // Represents one part
    public static class Book {

        private String booknum;
        private String title;
        private int qty;
        private int ucost;

        public Book(String booknum, String title,
                int qty, int ucost) {

            this.booknum = booknum;
            this.title = title;
            this.qty = qty;
            this.ucost = ucost;
        }

        public String getPartnum() {
            return booknum;
        }

        public String getDesc() {
            return title;
        }

        public int getQty() {
            return qty;
        }

        public int getUnitCost() {
            return ucost;
        }

        @Override
        public String toString() {
            return "Part number: " + booknum
                    + ", Description: " + title
                    + ", Quantity: " + qty
                    + ", Unit cost: " + ucost;
        }
    }

    
    
}

import java.io.IOException;

public class UseLibraryDB {

    public static void main(String[] args) {

        LibraryDB pdb = null;

        try {

            // Open the database
            pdb = new LibraryDB("library.db");

            // Populate the database if it is empty
            if (pdb.numRecs() == 0) {

                pdb.append(
                        "987654321012",
                        "Pride and Prejudice",
                        30,
                        2468
                );

                pdb.append(
                        "368543210123",
                        "To Kill a Mockingbird",
                        5,
                        1995
                );

                pdb.append(
                        "5842359461",
                        "50 shades darker",
                        22,
                        600
                );

                pdb.append(
                        "123456789012",
                        "Heartstopper",
                        26,
                        1500
                );

                pdb.append(
                        "6854321098766",
                        "Biografia de Daniel Alejandro Hidalgo Mendez",
                        9,
                        9000
                );
            }

            // Display all records
            dumpRecords(pdb);

            // Update record 1
            pdb.update(
                    1,
                    "368543210123",
                    "To Kill a Mockingbird",
                    10,
                    1895
            );

            // Display records again
            dumpRecords(pdb);

        } catch (IOException ioe) {

            System.err.println(ioe);

        } finally {

            // Close the database
            if (pdb != null) {
                pdb.close();
            }
        }
    }

    // Display all records
    static void dumpRecords(LibraryDB pdb) throws IOException {

        for (int i = 0; i < pdb.numRecs(); i++) {

            LibraryDB.Book part = pdb.select(i);

            System.out.print(
                    format(part.getPartnum(),
                            LibraryDB.IDLEN,
                            true
                    )
            );

            System.out.print(" | ");

            System.out.print(
                    format(part.getDesc(),
                            LibraryDB.TITLEN,
                            true
                    )
            );

            System.out.print(" | ");

            System.out.print(
                    format(
                            "" + part.getQty(),
                            10,
                            false
                    )
            );

            System.out.print(" | ");

            // Unit cost is stored in cents
            String s = part.getUnitCost() / 100
                    + "."
                    + part.getUnitCost() % 100;

            // Add a leading zero if necessary
            if (s.charAt(s.length() - 2) == '.') {
                s += "0";
            }

            System.out.println(
                    format(
                            s,
                            10,
                            false
                    )
            );
        }

        System.out.println(
                "Number of records = " + pdb.numRecs()
        );

        System.out.println();
    }

    // Format a string to a fixed width
    static String format(
            String value,
            int maxWidth,
            boolean leftAlign) {

        StringBuilder sb = new StringBuilder();

        int len = value.length();

        // Truncate if the value is too long
        if (len > maxWidth) {

            len = maxWidth;
            value = value.substring(0, len);
        }

        // Left-align
        if (leftAlign) {

            sb.append(value);

            for (int i = 0; i < maxWidth - len; i++) {
                sb.append(" ");
            }

        // Right-align
        } else {

            for (int i = 0; i < maxWidth - len; i++) {
                sb.append(" ");
            }

            sb.append(value);
        }

        return sb.toString();
    }
}


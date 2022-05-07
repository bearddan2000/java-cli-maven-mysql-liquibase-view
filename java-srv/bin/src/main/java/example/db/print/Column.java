package example.db.print;


import java.util.ArrayList;
import java.util.List;
/**
 * Represents a database table column.
 */
public class Column {

    /**
     * Column label.
     */
    private String label;

    /**
     * Generic SQL type of the column as defined in
     * <a target="_blank"
     * href="http://docs.oracle.com/javase/8/docs/api/java/sql/Types.html">
     * java.sql.Types
     * </a>.
     */
    private int type;

    /**
     * Generic SQL type name of the column as defined in
     * <a target="_blank"
     * href="http://docs.oracle.com/javase/8/docs/api/java/sql/Types.html">
     * java.sql.Types
     * </a>.
     */
    private String typeName;

    /**
     * Width of the column that will be adjusted according to column label
     * and values to be printed.
     */
    private int width = 0;

    /**
     * Column values from each row of a <code>ResultSet</code>.
     */
    private List<String> values = new ArrayList<>();

    /**
     * Flag for text justification using <code>String.format</code>.
     * Empty string (<code>""</code>) to justify right,
     * dash (<code>-</code>) to justify left.
     *
     * @see #justifyLeft()
     */
    private String justifyFlag = "";

    /**
     * Column type category. The columns will be categorised according
     * to their column types and specific needs to print them correctly.
     */
    private ECatagory typeCategory = ECatagory.OTHER;

    /**
     * Constructs a new <code>Column</code> with a column label,
     * generic SQL type and type name (as defined in
     * <a target="_blank"
     * href="http://docs.oracle.com/javase/8/docs/api/java/sql/Types.html">
     * java.sql.Types
     * </a>)
     *
     * @param label Column label or name
     * @param type Generic SQL type
     * @param typeName Generic SQL type name
     */
    public Column (String label, int type, String typeName) {
        this.label = label;
        this.type = type;
        this.typeName = typeName;
        this.width = label.length();
        this.typeCategory = ECatagory.whichCategory(type);
    }

    /**
     * Returns the column label
     *
     * @return Column label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the generic SQL type of the column
     *
     * @return Generic SQL type
     */
    public int getType() {
        return type;
    }

    /**
     * Returns the generic SQL type name of the column
     *
     * @return Generic SQL type name
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Returns the width of the column
     *
     * @return Column width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the column to <code>width</code>
     *
     * @param width Width of the column
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Adds a <code>String</code> representation (<code>value</code>)
     * of a value to this column object's {@link #values} list.
     * These values will come from each row of a
     * <a target="_blank"
     * href="http://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html">
     * ResultSet
     * </a> of a database query.
     *
     * @param value The column value to add to {@link #values}
     */
    public void addValue(String value) {
        values.add(value);
    }

    /**
     * Returns the column value at row index <code>i</code>.
     * Note that the index starts at 0 so that <code>getValue(0)</code>
     * will get the value for this column from the first row
     * of a <a target="_blank"
     * href="http://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html">
     * ResultSet</a>.
     *
     * @param i The index of the column value to get
     * @return The String representation of the value
     */
    public String getValue(int i) {
        return values.get(i);
    }

    /**
     * Returns the value of the {@link #justifyFlag}. The column
     * values will be printed using <code>String.format</code> and
     * this flag will be used to right or left justify the text.
     *
     * @return The {@link #justifyFlag} of this column
     * @see #justifyLeft()
     */
    public String getJustifyFlag() {
        return justifyFlag;
    }

    /**
     * Sets {@link #justifyFlag} to <code>"-"</code> so that
     * the column value will be left justified when printed with
     * <code>String.format</code>. Typically numbers will be right
     * justified and text will be left justified.
     */
    public void justifyLeft() {
        this.justifyFlag = "-";
    }

    /**
     * Returns the generic SQL type category of the column
     *
     * @return The {@link #typeCategory} of the column
     */
    public ECatagory getTypeCategory() {
        return typeCategory;
    }

    public void adjustWidth(String value){
      // Adjust the column width
      setWidth(value.length() > getWidth() ? value.length() : getWidth());
      addValue(value);
    }
}

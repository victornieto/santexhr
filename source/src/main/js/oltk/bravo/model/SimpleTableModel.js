//===========================================================================
//
// SimpleTableModel.js
//
//===========================================================================
oltk.namespace('oltk.bravo.model');


//===========================================================================
// SIMPLE TABLE MODEL
//===========================================================================
/**
 * Basic data model for a table.
 *
 * This model maintains the state of the table, and a list of listeners to
 * notify of changes.
 *
 * @class	oltk.bravo.model.SimpleTableModel
 * @author	bandur
 */
oltk.bravo.model.SimpleTableModel = function()
{
	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================

	/**
	 * The columns information.
	 */
	var _columns = [];

	/**
	 * The data, as a two-dimensional array.
	 */
	var _data = [];

	/**
	 * Change handlers.
	 */
	var _changeHandlers = [];


	//=======================================================================
	// PUBLIC METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// ADD TABLE CHANGED HANDLER
	//-----------------------------------------------------------------------
	/**
	 * Adds a handler for table-change events.
	 *
	 * @param	{function} handler - Callback that takes no parameter.
	 */
	this.addTableChangedHandler = function(handler)
	{
		_changeHandlers.push(handler);
	};

	//-----------------------------------------------------------------------
	// ADD COLUMN
	//-----------------------------------------------------------------------
	/**
	 * Adds a column to this model.
	 *
	 * @param	{object} metadata - Metadata about the column.
	 */
	this.addColumn = function(metadata)
	{
		_columns.push(metadata);
	};

	//-----------------------------------------------------------------------
	// ADD ROW
	//-----------------------------------------------------------------------
	/**
	 * Adds a row of data to this model.
	 *
	 * Fires a table-changed event.
	 *
	 * @param	{array} row - The row to add.
	 */
	this.addRow = function(row)
	{
		_data.push(row);

		_fireTableChanged();
	};

	//-----------------------------------------------------------------------
	// GET COLUMN
	//-----------------------------------------------------------------------
	/**
	 * Returns the metadata about the column at the given index.
	 *
	 * @param	{int} index - The index.
	 * @return	{object} The metadata.
	 */
	this.getColumn = function(index)
	{
		return _columns[index];
	};

	//-----------------------------------------------------------------------
	// GET COLUMN COUNT
	//-----------------------------------------------------------------------
	/**
	 * Returns the number of columns.
	 *
	 * @return	{int} The count.
	 */
	this.getColumnCount = function()
	{
		return _columns.length;
	};

	//-----------------------------------------------------------------------
	// GET DATA
	//-----------------------------------------------------------------------
	/**
	 * Returns the data contained in this model.
	 *
	 * @return	{array} The data, as an array of arrays.
	 */
	this.getData = function()
	{
		return _data;
	};

	//-----------------------------------------------------------------------
	// GET ROW COUNT
	//-----------------------------------------------------------------------
	/**
	 * Returns the number of rows in the data.
	 *
	 * @return	{int} The count.
	 */
	this.getRowCount = function()
	{
		return _data.length;
	};

	//-----------------------------------------------------------------------
	// GET VALUE AT
	//-----------------------------------------------------------------------
	/**
	 * Returns the value of the cell at the given indices.
	 *
	 * @param	{int} row - The row number.
	 * @param	{int} column - The column number.
	 * @return	{object} The value.
	 */
	this.getValueAt = function(row, column)
	{
		return _data[row][column];
	};

	//-----------------------------------------------------------------------
	// SET DATA
	//-----------------------------------------------------------------------
	/**
	 * Sets the complete data for this model using the given array of arrays.
	 *
	 * Fires a table-changed event.
	 *
	 * @param	{array} data - The array of arrays to use.
	 */
	this.setData = function(data)
	{
		_data = data;

		_fireTableChanged();
	};

	//-----------------------------------------------------------------------
	// SET VALUE AT
	//-----------------------------------------------------------------------
	/**
	 * Sets the value at the given indices.
	 *
	 * Fires a table-changed event.
	 *
	 * @param	{int} row - The row number.
	 * @param	{int} column - The column number.
	 * @param	{object} value - The value to set.
	 */
	this.setValueAt = function(row, column, value)
	{
		_data[row][column] = value;

		_fireTableChanged();
	};


	//=======================================================================
	// PRIVATE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// FIRE TABLE CHANGED
	//-----------------------------------------------------------------------
	/**
	 * @private
	 */
	var _fireTableChanged = function()
	{
		for (var i = 0; i < _changeHandlers.length; i++)
		{
			var handler = _changeHandlers[i];
			handler();
		}
	};
};

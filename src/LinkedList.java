public class LinkedList <T>
{
	// class to represent one node in a list
	private class Node<T> 
	{
		// package access members; List can access these directly
		T data;
		Node<T> nextNode;

		/**
		 * constructor creates a ListNode that refers to data
		 */
		Node(T data) {
			this(data, null);
		}

		/**
		 * constructor creates ListNode that refers to data and to next ListNode
		 */
		Node(T data, Node<T> node) {
			this.data = data;
			nextNode = node;
		}

		/**
		 * return reference to data
		 * 
		 * @return
		 */
		T getObject() {
			return data;
		}

		/**
		 * return reference to next node in list
		 */
		Node<T> getNext() {
			return nextNode;
		}

	} // end inner class Node
	private Node<T> firstNode;
	private Node<T> lastNode;
	private String name; // string like "list" used in printing
	private int size;

	/**
	 * constructor creates empty List with "listName" as the name
	 */
	public LinkedList() {
		this("list");
	}

	/*
	 * constructor creates an empty List with a name
	 */
	public LinkedList(String listName) {
		name = listName;
		firstNode = lastNode = null;
		size=0;
	}
	
	public T getFirst()
	{
		return firstNode.data;
	}

	/**
	 * Inserts a new item at the front of the list
	 */
	public void insertAtFront(T insertItem) {
		Node<T> node = new Node<T>(insertItem);
		
		if (isEmpty()) // firstNode and lastNode refer to same object
			firstNode = lastNode = node;
		else { // firstNode refers to new node
			node.nextNode = firstNode;
			firstNode = node;
			// you can replace the two previous lines with this line: firstNode
			// = new ListNode( insertItem, firstNode );
		}
		size++;
	} // end method insertAtFront

	/**
	 *  insert item at end of List
	 */
	public void insertAtBack(T insertItem) {
		Node<T> node = new Node<T>(insertItem);
		
		if (isEmpty()) // firstNode and lastNode refer to same Object
			firstNode = lastNode = node;
		else { // lastNode's nextNode refers to new node
			lastNode.nextNode = node;
			lastNode = node;
			// you can replace the two previous lines with this line: lastNode =
			// lastNode.nextNode = new ListNode( insertItem );
		}
		size++;
	} // end method insertAtBack

	/**
	 *  remove first node from List
	 *  throws EmptyListException if the list is empty 
	 */
	public T removeFromFront() throws EmptyListException 
	{
		if (isEmpty()) // throw exception if List is empty
			throw new EmptyListException(name);

		T removedItem = firstNode.data; // retrieve data being removed

		// update references firstNode and lastNode
		if (firstNode == lastNode)
			firstNode = lastNode = null;
		else
			firstNode = firstNode.nextNode;
		size--;
		return removedItem; // return removed node data
	} // end method removeFromFront

	/**
	 *  remove last node from List
	 * throws EmptyListException if the list is empty
	 */
	public T removeFromBack() throws EmptyListException {
		if (isEmpty()) // throw exception if List is empty
			throw new EmptyListException(name);

		T removedItem = lastNode.data; // retrieve data being removed

		// update references firstNode and lastNode
		if (firstNode == lastNode)
			firstNode = lastNode = null;
		else // locate new last node
		{
			Node<T> current = firstNode;

			// loop while current node does not refer to lastNode
			while (current.nextNode != lastNode)
				current = current.nextNode;

			lastNode = current; // current is new lastNode
			current.nextNode = null;
		} // end else
		size--;
		return removedItem; // return removed node data
	} // end method removeFromBack

	/**
	 *  determine whether list is empty
	 */
	public boolean isEmpty() {
		return firstNode == null; // return true if List is empty
	} // end method isEmpty

	public int getSize()
	{
		return size;
	}
	/**
	 *  output List contents
	 */
	public void print() {
		if (isEmpty()) {
			System.out.printf("Empty %s\n", name);
			return;
		} // end if

		System.out.printf("The %s is: \n", name);
		Node<T> current = firstNode;

		// while not at end of list, output current node's data
		while (current != null) {
			System.out.printf("%s \n", current.data);
			current = current.nextNode;
		} // end while

	} // end method print

}

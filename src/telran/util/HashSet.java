package telran.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class HashSet<T> implements Set<T> {
	private static final int DEFAULT_HASH_TABLE_LENGTH = 16;
	private static final float DEFAULT_FACTOR = 0.75f;
	List<T>[] hashTable;
	int size;
	float factor;

	private class HashSetIterator implements Iterator<T> {
		private List<T> currentList;
		private int current;

		public HashSetIterator() {
			this.currentList = getList();
			this.current = 0;
		}

		private List<T> getList() {
			List<T> listTmp = new ArrayList<>();
			for (List<T> list : hashTable) {
				if (list != null) {
					int i = 0;
					for (T obj : list) {
						listTmp.add(i++, obj);
					}
				}
			}
			return listTmp;
		}

		@Override
		public boolean hasNext() {

			return current < size;
		}

		@Override
		public T next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			return currentList.get(current++);
		}
	}

	public HashSet(int hashTableLength, float factor) {
		hashTable = new List[hashTableLength];
		this.factor = factor;
	}

	public HashSet() {
		this(DEFAULT_HASH_TABLE_LENGTH, DEFAULT_FACTOR);
	}

	@Override
	public boolean add(T obj) {
		boolean res = false;
		if (!contains(obj)) {
			if ((float) size / hashTable.length >= factor) {
				hashTableReallocation();
			}
			addObjInHashTable(obj, hashTable);
			size++;
			res = true;
		}

		return res;
	}

	private void hashTableReallocation() {
		List<T>[] tmp = new List[hashTable.length * 2];
		for (List<T> list : hashTable) {
			if (list != null) {
				for (T obj : list) {
					addObjInHashTable(obj, tmp);
				}
			}
		}
		hashTable = tmp;

	}

	private void addObjInHashTable(T obj, List<T>[] table) {
		int index = getIndex(obj, table);
		List<T> list = table[index];
		if (list == null) {
			list = new LinkedList<>();
			table[index] = list;
		}
		list.add(obj);

	}

	private int getIndex(T obj, List<T>[] table) {
		int hashCode = obj.hashCode();
		int index = Math.abs(hashCode % table.length);
		return index;

	}

	private int getIndex(T obj) {
		int hashCode = obj.hashCode();
		int index = Math.abs(hashCode % hashTable.length);
		return index;
	}

	@Override
	public boolean remove(T pattern) {
		boolean res = false;
		int index = getIndex(pattern);
		List<T> list = hashTable[index];
		if (list != null) {
			res = list.remove(pattern);
		}
		if (res)
			size--;
		return res;
	}

	@Override
	public boolean contains(T pattern) {
		int index = getIndex(pattern);
		List<T> list = hashTable[index];
		return list != null && list.contains(pattern);
	}

	@Override
	public int size() {

		return size;
	}

	@Override
	public Iterator<T> iterator() {

		return new HashSetIterator();
	}

	@Override
	public T get(T pattern) {
		int index = getIndex(pattern);
		List<T> list = hashTable[index];
		boolean res = false;
		if (list != null) {
			res = list.contains(pattern);
		}

		return res ? pattern : null;

	}

}

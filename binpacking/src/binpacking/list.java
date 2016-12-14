package binpacking;

import java.io.File;
import java.util.Scanner;

public class list {
	// ù��° ��带 ����Ű�� �ʵ� �ʱⰪ���� head �� null�� ��带 �����ϰ� tail�� head�� ����Ű�� �Ѵ�.
	public Node head = new Node(null);
	public Node tail = head;
	public int size = 0; // ����Ʈ�� ����� �˷��ִ� ����

	/**
	 * �ΰ��� ���簢���� ���� ������ �ִ��� Ȯ���Ѵ�(������ �ִٴ� ���� ���� ��ġ���� ���԰��迡 ���ϴ� ��)
	 * 
	 * @param r1
	 *            ���� ����
	 * @param r2
	 *            �־��� ����
	 * @return false �� ���� ������ ���°� true�� ������ �ִ°�
	 */
	public static boolean isIntersect(Form r1, Form r2) {
		if (r1.x >= r2.x + r2.w)
			return false;
		if (r1.x + r1.w <= r2.x)
			return false;
		if (r1.y >= r2.y + r2.h)
			return false;
		if (r1.y + r1.h <= r2.y)
			return false;
		return true;
	}

	/**
	 * ������ ������ �־����� �ִ��� ���θ� Ȯ���ϴ� �޼ҵ��̴�.
	 * 
	 * @param r1
	 *            ���� ����
	 * @param r2
	 *            �־��� ����
	 * @return true�� �־����� �ִ� false �ִ� ���� �Ұ����ϴ�.
	 */
	public static boolean isInclude(Form r1, Form r2) {
		if (r2.x >= r1.x && r2.x + r2.w <= r1.x + r1.w && r2.y >= r1.y && r2.y + r2.h <= r1.y + r1.h)
			return true;
		return false;
	}

	/**
	 * Ž���� �����ϴ� �޼ҵ��̴�. �������� �˰���
	 * 
	 * @param testcase
	 *            = testcase�� �� �迭
	 * @param width
	 *            = �ʺ� �Ǵ� �Ű�����
	 * @param height
	 *            = ���̰� �Ǵ� �Ű�����
	 * @param total
	 *            = �� ������ �� ���� �־��� ������ �� total�� �����ϸ� ��� ����Ǿ��ٴ� �ǹ�
	 * @return
	 */
	public static double start(int testcase[], int width, int height, int total) {
		int numcase = testcase.length / 2;

		list itemList = new list(); // ������ �� ����Ʈ
		list spaceList = new list(); // ���� ������ �� ����Ʈ

		for (int i = 0; i < numcase; i++) {
			Form item = new Form();
			item.w = testcase[i * 2];
			item.h = testcase[i * 2 + 1];
			itemList.insert(item); // ������ ����
		}

		spaceList.push(new Form(0, 0, width, height)); // ��������Ʈ�� ����

		int u = 0;// ���� ����
		int la = 0; // �ܿ� ����
		Node item = itemList.head.next; // ù��° ���� ����Ű��
		while (item != null) {
			boolean isFound = false; // �ϴ� ������ �������� ������ Ȯ��
			Node space = spaceList.head.next;

			while (space != null) {
				// ���⼭�� ȸ���� ���� ���� random���� ���� �����Ѵ�.
				// r�� 1�̸� ȸ�� r�� 0�̸� �״��
				int r = (int) (Math.random() + 0.5);
				boolean bool2 = false;
				if (r == 1)
					bool2 = true;
				int temp;
				if (bool2) {
					temp = item.data.w;
					item.data.w = item.data.h;
					item.data.h = temp;
				}
				// ������ �ʺ� ������ �ʺ񺸴� �۰� ������ ���̰� ������ ���̺��� �۴ٸ� true�� return �Ѵ�.
				if (item.data.w <= space.data.w && item.data.h <= space.data.h) {
					isFound = true;
					break;
				}
				// ���� while���� ������ ���Ͽ��ٸ� ������ ȸ�����Ѽ� ������ ���� �ִ��� ���θ� Ȯ���Ѵ�.
				temp = item.data.w;
				item.data.w = item.data.h;
				item.data.h = temp;
				if (item.data.w <= space.data.w && item.data.h <= space.data.h) {
					isFound = true;
					break;
				}
				// ��������� ������ ������ ���� ���ٴ� ���̹Ƿ� ���� ������ Ȯ���ϵ��� �Ѵ�.
				space = space.next;
			}
			// ������ �� �������� �۴ٸ�
			if (isFound) {
				item.data.x = space.data.x;
				item.data.y = space.data.y;
				u += item.data.w * item.data.h; // �� ���� ���� ���緮�� ������

				// ��⼭ ��������
				// System.out.println("item.data.x = " + item.data.x +
				// "item.data.y = " + item.data.y + "item.data.w"
				// + item.data.w + "item.data.h" + item.data.h);
				list newList = new list(); // ���Ӱ� ��������� ������ ���� ����Ʈ
				Node sp = spaceList.head.next; // ó�� �������� ����
				while (sp != null) {
					if (isInclude(item.data, sp.data)) { // ������ ���ǿ� ���ԵǸ� ���� �Ұ�
						sp = spaceList.erase(sp); // ��������
						continue;
					}

					if (isIntersect(item.data, sp.data)) { // ������ ������ ���� �������谡
						// �ִ��� Ȯ��
						Form rt = new Form(sp.data.x, sp.data.y, sp.data.w, item.data.y - sp.data.y);
						Form rb = new Form(sp.data.x, item.data.y + item.data.h, sp.data.w,
								sp.data.y + sp.data.h - (item.data.y + item.data.h));
						Form rl = new Form(sp.data.x, sp.data.y, item.data.x - sp.data.x, sp.data.h);
						Form rr = new Form(item.data.x + item.data.w, sp.data.y,
								sp.data.x + sp.data.w - (item.data.x + item.data.w), sp.data.h);
						// ���Ӱ� ������ ���� newList�� ����
						if (rt.w > 0 && rt.h > 0)
							newList.push(rt);
						if (rb.w > 0 && rb.h > 0)
							newList.push(rb);
						if (rl.w > 0 && rl.h > 0)
							newList.push(rl);
						if (rr.w > 0 && rr.h > 0)
							newList.push(rr);
						sp = spaceList.erase(sp);// ������ ���������� ���� ���� ����
						continue;
					}

					sp = sp.next; // ���ǰ� ������ ���� ���谡 ���� ���� ������ Ȯ���Ѵ�.
				}
				// ���⼭ ���� ���ο� ������ ���������� ħ�� �Ǵ� ��ģ�ٸ� �׺κ��� �����Ѵ� >> ���ο� ������ ����
				Node s1, s2;
				s1 = spaceList.head.next;
				while (s1 != null) {
					s2 = newList.head.next;
					while (s2 != null) {

						if (s1 != s2 && isInclude(s1.data, s2.data)) {// ���ο� ������
							// ����
							// ������
							// �����Ѵٸ�
							// ���ο�
							// ���� ����
							s2 = newList.erase(s2);
							continue;
						}
						s2 = s2.next;
					}
					s1 = s1.next;
				}
				s1 = newList.head.next; // ���⼭���ʹ� �ݴ�� ���ο� ������ ���������� ���ԵǴ��� Ȯ����
				// �Ѵ�.
				while (s1 != null) {
					s2 = newList.head.next;
					while (s2 != null) {
						// �������������ο� ������ �����Ѵٸ���� ��������
						if (s1 != s2 && isInclude(s1.data, s2.data)) {
							s2 = newList.erase(s2); // ���� ������ �����ϰ� ���� ���� ����
							continue;
						}
						s2 = s2.next;
					}
					s1 = s1.next;
				}

				spaceList.merge(newList); // ���Ӱ� ������������� ���̴� ���� ���� ���� �ٸ� ��������
				// �����ϰ� �ȴ�.
			}
			if (!isFound) {
				la += item.data.w * item.data.h; // ������� ���� ����
			}
			item = item.next; // ���� ���� �˻�
		}
		return Math.floor(u / (double) (total) * 10000) / 100; // ����� ����
	}

	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
			Scanner scan = new Scanner(new File("cking_input.txt"));
			int test = scan.nextInt();
			int count = 1;
			while (test-- > 0) {

				int number = scan.nextInt();
				int width = scan.nextInt();
				int total = 0;
				int min = 0;
				int len = 0;
				int array[] = new int[number * 2];
				for (int i = 0; i < number; i++) {
					array[i * 2] = scan.nextInt();
					array[i * 2 + 1] = scan.nextInt();
					total += array[i * 2] * array[i * 2 + 1];
					if (array[i * 2] > min)
						min = array[i * 2];
					if (array[i * 2 + 1] > min)
						min = array[i * 2 + 1];
					len += min;
				}
				int left = min;
				int right = len;
				int result = 987654321;
				// �̺�Ž���� �ι��ϴ� ������ ���α��̿� ���α��̸� �ٲپ ������ ����� �� ���� ��찡 �ֱ� ������
				int answer = 987654321;
				for (int i = 0; i < 1000; i++) { // �������� ȸ�� 1000���� ���� ����
					// optimize�� ����� ���
					while (left <= right) {
						int mid = (left + right) / 2;
						double k = start(array, width, mid, total);
						if (k < 100.0) {
							left = mid + 1;
						} else if (k >= 100.0) {
							right = mid - 1;
							if (result > mid) {
								result = mid;
							}
						}
					}
					left = min;
					right = len;
					while (left <= right) {
						int mid = (left + right) / 2;
						double k = start(array, mid, width, total);
						if (k < 100.0) {
							left = mid + 1;
						} else if (k >= 100.0) {
							right = mid - 1;
							if (result > mid) {
								result = mid;
							}
						}
					}
					// �̺κ��� 8�� �׽�Ʈ ���̽��� �ְ��� ����� ����ϱ� ���ؼ� 100���� �� ���� �Ǵ� ��츦
					// ����ߴ�.

					if (answer > result) {
						answer = result;
					}

				}
				long end = System.currentTimeMillis();
				System.out.print("testcase" + (count++) + " ���� : " + answer + " ");
				System.out.println("����ð� : " + (end - start) / 1000.0);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	class Node {
		// �����Ͱ� ����� �ʵ�
		public Form data;
		// ���� ��带 ����Ű�� �ʵ�
		public Node next;
		public Node prev;

		public Node(Form input) {
			this.data = input;
			this.next = null;
			this.prev = null;
		}
	}

	public void merge(list list) {
		if (list.size < 1)
			return;
		this.tail.next = list.head.next;
		list.head.next.prev = this.tail;
		this.tail = list.tail;
		this.size += list.size;
	}

	public int insert(Form input) {
		int n = 0;
		boolean isFound = false;
		Node newNode = new Node(input);
		Node node = this.head.next;
		newNode.prev = this.head;
		while (node != null) {
			++n;
			if (node.data.w * node.data.h > input.w * input.h) {
				newNode.prev = node;
				node = node.next;
			} else {
				newNode.next = node;
				node.prev.next = newNode;
				node.prev = newNode;
				isFound = true;
				++this.size;
				break;
			}

		}
		if (!isFound) {
			this.push(newNode.data);
		}
		return n;
	}

	public Node erase(Node node) {
		if (node == this.head) {
			System.out.println("Do not erase headnode");
			return null;
		}
		if (node == this.tail)
			this.tail = this.tail.prev;
		node.prev.next = node.next;
		if (node.next != null) {
			node.next.prev = node.prev;
		}
		--this.size;
		return node.next;

	}

	public void push(Form element) {
		Node node = this.tail;
		node.next = new Node(element);
		node.next.prev = node;
		this.tail = node.next;
		++this.size;
	}

	Node node(int index) {
		// ����� �ε����� ��ü ��� ���� �ݺ��� ū�� ������ ���
		if (index < size / 2) {
			// head���� next�� �̿��ؼ� �ε����� �ش��ϴ� ��带 ã���ϴ�.
			Node x = head;
			for (int i = 0; i < index; i++)
				x = x.next;
			return x;
		} else {
			// tail���� prev�� �̿��ؼ� �ε����� �ش��ϴ� ��带 ã���ϴ�.
			Node x = tail;
			for (int i = size - 1; i > index; i--)
				x = x.prev;
			return x;
		}
	}
}

class Form {
	public int x;
	public int y;
	public int w;
	public int h;

	Form() {
		this.x = 0;
		this.y = 0;
		this.w = 0;
		this.h = 0;
	}

	Form(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
}

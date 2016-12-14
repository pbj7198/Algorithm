package binpacking;

import java.io.File;
import java.util.Scanner;

public class list {
	// 첫번째 노드를 가리키는 필드 초기값으로 head 에 null인 노드를 삽입하고 tail도 head를 가르키게 한다.
	public Node head = new Node(null);
	public Node tail = head;
	public int size = 0; // 리스트의 사이즈를 알려주는 변수

	/**
	 * 두개의 직사각형이 서로 관련이 있는지 확인한다(관련이 있다는 것은 서로 겹치던가 포함관계에 속하는 것)
	 * 
	 * @param r1
	 *            넣을 후판
	 * @param r2
	 *            넣어질 공간
	 * @return false 는 서로 관련이 없는것 true는 관련이 있는것
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
	 * 후판이 공간에 넣어질수 있는지 여부를 확인하는 메소드이다.
	 * 
	 * @param r1
	 *            넣을 후판
	 * @param r2
	 *            넣어질 공간
	 * @return true면 넣어질수 있다 false 넣는 것이 불가능하다.
	 */
	public static boolean isInclude(Form r1, Form r2) {
		if (r2.x >= r1.x && r2.x + r2.w <= r1.x + r1.w && r2.y >= r1.y && r2.y + r2.h <= r1.y + r1.h)
			return true;
		return false;
	}

	/**
	 * 탐색을 시작하는 메소드이다. 본격적인 알고리즘
	 * 
	 * @param testcase
	 *            = testcase가 들어간 배열
	 * @param width
	 *            = 너비가 되는 매개변수
	 * @param height
	 *            = 높이가 되는 매개변수
	 * @param total
	 *            = 각 후판의 총 넓이 넣어진 공간과 총 total이 동일하면 모두 적재되었다는 의미
	 * @return
	 */
	public static double start(int testcase[], int width, int height, int total) {
		int numcase = testcase.length / 2;

		list itemList = new list(); // 후판이 들어갈 리스트
		list spaceList = new list(); // 남은 공간이 들어갈 리스트

		for (int i = 0; i < numcase; i++) {
			Form item = new Form();
			item.w = testcase[i * 2];
			item.h = testcase[i * 2 + 1];
			itemList.insert(item); // 삽입후 정렬
		}

		spaceList.push(new Form(0, 0, width, height)); // 공간리스트에 삽입

		int u = 0;// 적재 면적
		int la = 0; // 잔여 면적
		Node item = itemList.head.next; // 첫번째 후판 가르키기
		while (item != null) {
			boolean isFound = false; // 일단 후판이 공간보다 작은지 확인
			Node space = spaceList.head.next;

			while (space != null) {
				// 여기서는 회전을 할지 말지 random으로 값을 지정한다.
				// r이 1이면 회전 r이 0이면 그대로
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
				// 후판의 너비가 공간의 너비보다 작고 후판의 높이가 공간의 높이보다 작다면 true를 return 한다.
				if (item.data.w <= space.data.w && item.data.h <= space.data.h) {
					isFound = true;
					break;
				}
				// 만약 while문을 나가지 못하였다면 후판을 회전시켜서 공간에 들어갈수 있는지 여부를 확인한다.
				temp = item.data.w;
				item.data.w = item.data.h;
				item.data.h = temp;
				if (item.data.w <= space.data.w && item.data.h <= space.data.h) {
					isFound = true;
					break;
				}
				// 결과적으로 후판이 공간에 들어갈수 없다는 것이므로 다음 공간을 확인하도록 한다.
				space = space.next;
			}
			// 후판이 들어갈 공간보다 작다면
			if (isFound) {
				item.data.x = space.data.x;
				item.data.y = space.data.y;
				u += item.data.w * item.data.h; // 들어갈 후판 넓이 적재량에 더해줌

				// 요기서 값가져왕
				// System.out.println("item.data.x = " + item.data.x +
				// "item.data.y = " + item.data.y + "item.data.w"
				// + item.data.w + "item.data.h" + item.data.h);
				list newList = new list(); // 새롭게 만들어지는 공간을 넣을 리스트
				Node sp = spaceList.head.next; // 처음 공간부터 시작
				while (sp != null) {
					if (isInclude(item.data, sp.data)) { // 공간이 후판에 포함되면 삽입 불가
						sp = spaceList.erase(sp); // 공간삭제
						continue;
					}

					if (isIntersect(item.data, sp.data)) { // 공간과 후판이 서로 연관관계가
						// 있는지 확인
						Form rt = new Form(sp.data.x, sp.data.y, sp.data.w, item.data.y - sp.data.y);
						Form rb = new Form(sp.data.x, item.data.y + item.data.h, sp.data.w,
								sp.data.y + sp.data.h - (item.data.y + item.data.h));
						Form rl = new Form(sp.data.x, sp.data.y, item.data.x - sp.data.x, sp.data.h);
						Form rr = new Form(item.data.x + item.data.w, sp.data.y,
								sp.data.x + sp.data.w - (item.data.x + item.data.w), sp.data.h);
						// 새롭게 나오는 공간 newList에 삽입
						if (rt.w > 0 && rt.h > 0)
							newList.push(rt);
						if (rb.w > 0 && rb.h > 0)
							newList.push(rb);
						if (rl.w > 0 && rl.h > 0)
							newList.push(rl);
						if (rr.w > 0 && rr.h > 0)
							newList.push(rr);
						sp = spaceList.erase(sp);// 공간이 나뉘었으니 기존 공간 제거
						continue;
					}

					sp = sp.next; // 후판과 공간이 서로 관계가 없어 다음 공간을 확인한다.
				}
				// 여기서 부터 새로운 공간이 기존공간을 침범 또는 겹친다면 그부분을 제거한다 >> 새로운 공간을 생성
				Node s1, s2;
				s1 = spaceList.head.next;
				while (s1 != null) {
					s2 = newList.head.next;
					while (s2 != null) {

						if (s1 != s2 && isInclude(s1.data, s2.data)) {// 새로운 공간을
							// 기존
							// 공간이
							// 포함한다면
							// 새로운
							// 공간 제거
							s2 = newList.erase(s2);
							continue;
						}
						s2 = s2.next;
					}
					s1 = s1.next;
				}
				s1 = newList.head.next; // 여기서부터는 반대로 새로운 공간에 기존공간이 포함되는지 확인을
				// 한다.
				while (s1 != null) {
					s2 = newList.head.next;
					while (s2 != null) {
						// 기존공간을새로운 공간이 포함한다면기존 공간제거
						if (s1 != s2 && isInclude(s1.data, s2.data)) {
							s2 = newList.erase(s2); // 현재 공간을 제거하고 다음 공간 리턴
							continue;
						}
						s2 = s2.next;
					}
					s1 = s1.next;
				}

				spaceList.merge(newList); // 새롭게 만들어진공간을 붙이는 역할 서로 각기 다른 공간들이
				// 공존하게 된다.
			}
			if (!isFound) {
				la += item.data.w * item.data.h; // 적재되지 못한 후판
			}
			item = item.next; // 다음 후판 검사
		}
		return Math.floor(u / (double) (total) * 10000) / 100; // 적재률 리턴
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
				// 이분탐색을 두번하는 이유는 가로길이와 세로길이를 바꾸어서 나오는 결과가 더 좋은 경우가 있기 때문에
				int answer = 987654321;
				for (int i = 0; i < 1000; i++) { // 포문으로 회전 1000번을 통해 제일
					// optimize한 결과를 출력
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
					// 이부분은 8개 테스트 케이스의 최고의 결과만 출력하기 위해서 100번중 이 값이 되는 경우를
					// 출력했다.

					if (answer > result) {
						answer = result;
					}

				}
				long end = System.currentTimeMillis();
				System.out.print("testcase" + (count++) + " 정답 : " + answer + " ");
				System.out.println("실행시간 : " + (end - start) / 1000.0);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	class Node {
		// 데이터가 저장될 필드
		public Form data;
		// 다음 노드를 가리키는 필드
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
		// 노드의 인덱스가 전체 노드 수의 반보다 큰지 작은지 계산
		if (index < size / 2) {
			// head부터 next를 이용해서 인덱스에 해당하는 노드를 찾습니다.
			Node x = head;
			for (int i = 0; i < index; i++)
				x = x.next;
			return x;
		} else {
			// tail부터 prev를 이용해서 인덱스에 해당하는 노드를 찾습니다.
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

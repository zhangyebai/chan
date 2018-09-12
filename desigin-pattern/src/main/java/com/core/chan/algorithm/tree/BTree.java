package com.core.chan.algorithm.tree;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class BTree {
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	@Accessors(chain = true)
	@ToString
	private static final class Node{
		private Node left;
		private Node right;
		private Object data;
	}
	/** test method  **/
	public static void test(){
		BTree bTree = new BTree();
		bTree.set("G");
		bTree.set("B");
		bTree.set("A");
		bTree.set("C");
		bTree.set("M");
		bTree.set("H");
		bTree.set("N");
		bTree.remove("G");
		System.out.println(bTree.size());
		bTree.pre().forEach(node->
			System.out.print(node.getData() + " ")
		);
		System.out.println();
		bTree.mid().forEach(node->
				System.out.print(node.getData() + " ")
		);
		System.out.println();
		bTree.back().forEach(node->
				System.out.print(node.getData() + " ")
		);
		System.out.println();
		System.out.println("root = " + bTree.getRoot().getData());

		bTree.remove("B");
		bTree.pre().forEach(node->
				System.out.print(node.getData() + " ")
		);
		System.out.println();
		bTree.remove("M");
		bTree.pre().forEach(node->
				System.out.print(node.getData() + " ")
		);
		System.out.println();
		System.out.println("root = " + bTree.getRoot().getData());
	}

	private Node root;
	private int size;


	/**
	 * step 1 : data is null, return
	 * step 2 : root is null, put data on root
	 * step 3 : turn right or turn left depends on cursor'data'hashcode
	 *
	 * */
	public int size(){
		return this.size;
	}

	public void set(Object data){
		if(data == null){
			return;
		}
		Node node = new Node().setData(data);
		if(root == null){
			this.root = node;
			this.size++;
			return;
		}
		int code = data.hashCode();
		Node cursor = this.root;
		for(;;){
			if(code > cursor.getData().hashCode()){
				if(cursor.getRight() == null){
					cursor.setRight(node);
					this.size++;
					break;
				}else{
					cursor = cursor.getRight();
				}
			}else if(code < cursor.getData().hashCode()){
				if(cursor.getLeft() == null){
					cursor.setLeft(node);
					this.size++;
					break;
				}else{
					cursor = cursor.getLeft();
				}
			}else{
				break;
			}
		}
	}

	public Node get(Object data){
		if(this.size <= 0 || data == null){
			return null;
		}
		Node cursor = this.root;
		int code = data.hashCode();
		for(;cursor != null;){
			if(code > cursor.getData().hashCode()){
				cursor = cursor.getRight();
			}else if(code < cursor.getData().hashCode()){
				cursor = cursor.getLeft();
			}else{
				return cursor;
			}
		}
		return null;
	}

	/**
	 * 				A
	 * 			B		C
	 * 		  D  E	  F	  G
	 * 		  pre: A BDE CFG
	 * 		  mid: DBE A FCG
	 * 		  back : DEB FGC A
	 *
	 * 		  **非递归遍历需要使用栈结构保存左右树的遍历路径**
	 * */
	public List<Node> pre(){
		List<Node> list = new ArrayList<>();
		if(this.root == null){
			return list;
		}
		list.add(this.root);
		preErgodic(list, this.root.getLeft());
		preErgodic(list, this.root.getRight());
		return list;
	}
	private void preErgodic(List<Node> list, Node node){
		if(null == node){
			return;
		}
		list.add(node);
		preErgodic(list, node.getLeft());
		preErgodic(list, node.getRight());
	}
	public List<Node> mid(){
		List<Node> list = new ArrayList<>();
		if(this.root == null){
			return list;
		}
		midErgodic(list, this.root.getLeft());
		list.add(this.root);
		midErgodic(list, this.root.getRight());
		return list;
	}
	private void midErgodic(List<Node> list, Node node){
		if(null == node){
			return;
		}
		midErgodic(list, node.getLeft());
		list.add(node);
		midErgodic(list, node.getRight());
	}

	public List<Node> back(){
		List<Node> list = new ArrayList<>();
		if(this.root == null){
			return list;
		}
		backErgodic(list, this.root.getLeft());
		backErgodic(list, this.root.getRight());
		list.add(this.root);
		return list;
	}

	private void backErgodic(List<Node> list, Node node){
		if(null == node){
			return ;
		}
		backErgodic(list, node.getLeft());
		backErgodic(list, node.getRight());
		list.add(node);
	}

	/**
	 * 删除某节点需要操作次节点、父节点、子节点, 其中节点中包含l、r子节点信息,所以还需要查找父节点
	 * 本BinaryTree的实现并不保证Node中data的强一致性,因为根据hashcode去定位而不是根据 == or equals去比较定位
	 * step 1: 移除root节点
	 * 		1.1: root 无左右节点
	 * 		1.2: root 有左节点无右节点
	 * 		1.3: root 无左节点有右节点
	 * 		1.4: root 有左节点有右节点
	 * step 2: 移除左子树节点
	 * step 3: 移除右子树节点
	 * */
	public boolean remove(Object data){
		if (null == data || null == this.root){
			return false;
		}

		/**不宜使用节点查找方法去定位需要删除的节点,因为得不到父节点信息
		Node cursor = get(data);
		if(null == cursor){
			return false;
		}*/
		int code = data.hashCode();
		//Node cursor = this.root;
		if(code < this.root.getData().hashCode()){
			return removeLeft(data);
		}else if(code > this.root.getData().hashCode()){
			return removeRight(data);
		}else{
			return removeRoot();
		}
	}

	/**
	 * 如果root节点存在右子树的话,永远考虑提升右子树的左节点作为新的root节点
	 * 所以此函数中的一个if条件判断可以略去,详见注释部分的实现
	 * */
	private boolean removeRoot(){

		if(null == this.root.getRight()){
			if(null == this.root.getLeft()){
				this.root = null;
				this.size = 0;
				return true;
			}else{
				Node cursor = this.root.getLeft();
				this.root.setLeft(null);
				this.root = cursor;
				this.size--;
				return true;
			}
		}else{
			/**
			 * step 1: 找右子树的最左节点
			 * step 2: 改变最左节点的父节点的left指向
			 * notice:右子树的顶端节点即为最左节点的时候,需要特殊处理()
			 * */
			Node parent = this.root;
			Node cursor = parent.getRight();
			for(; null != cursor.getLeft();){
				parent = cursor;
				cursor = cursor.getLeft();
			}

			Node left = this.root.getLeft();
			Node right = this.root.getRight();
			this.root.setLeft(null);
			this.root.setRight(null);
			this.root = cursor;
			this.root.setLeft(left);
			//说明上面的for循环走进去了,右子树有左节点
			if(!(cursor.equals(this.root.getRight()) && parent.equals(this.root))){
				parent.setLeft(null);

				/// 如果上面的for循环没走进去的话,不能设置此右节点
				this.root.setRight(right);
			}
			this.size--;
			return true;
		}


		/*if(null == this.root.getLeft() && null == this.root.getRight()){
			this.root = null;
			this.size = 0;
			return true;
		}else if( null != this.root.getLeft() && null == this.root.getRight()){
			Node cursor = this.root.getLeft();
			this.root.setLeft(null);
			this.root = cursor;
			this.size--;
			return true;
		}else if(null == this.root.getLeft() && null != this.root.getRight()){
			Node cursor = this.root.getRight();
			this.root.setRight(null);
			this.root = cursor;
			this.size--;
			return true;
		}else if(null != this.root.getLeft() && null != this.root.getRight()){
			Node parent = this.root;
			Node cursor = parent.getRight();
			for(; null != cursor.getLeft();){
				parent = cursor;
				cursor = cursor.getLeft();
			}

			Node left = this.root.getLeft();
			Node right = this.root.getRight();
			this.root.setLeft(null);
			this.root.setRight(null);
			this.root = cursor;
			this.root.setLeft(left);
			//说明上面的for循环走进去了,右子树有左节点
			if(!(cursor.equals(this.root.getRight()) && parent.equals(this.root))){
				parent.setLeft(null);

				/// 如果上面的for循环没走进去的话,不能设置此右节点
				this.root.setRight(right);
			}
			this.size--;
			return true;
		}
		return false;*/
	}

	private boolean removeLeft(Object data){
		Node parent = this.root;
		Node cursor = parent.getLeft();
		boolean find = false;
		int code = data.hashCode();
		for(; null != cursor;){
			int t = cursor.getData().hashCode();
			if(code > t){
				parent = cursor;
				cursor = cursor.getRight();
			}else if(code < t){
				parent = cursor;
				cursor = cursor.getLeft();
			}else{
				find = true;
				break;

			}
		}
		if(!find){
			return false;
		}
		Node left = cursor.getLeft();
		Node right = cursor.getRight();
		Node orphan = voteRoot(cursor);
		if(!orphan.equals(left)) {
			orphan.setLeft(left);
		}
		if(!orphan.equals(right)){
			orphan.setRight(right);
		}
		parent.setLeft(orphan);
		this.size--;
		return true;
	}

	private boolean removeRight(Object data){
		Node parent = this.root;
		Node cursor = parent.getRight();
		int code = data.hashCode();
		boolean find = false;
		for(; null != cursor;){
			int t = cursor.getData().hashCode();
			if(code > t){
				parent = cursor;
				cursor = cursor.getRight();
			}else if(code < t){
				parent = cursor;
				cursor = cursor.getLeft();
			}else{
				find = true;
				break;
			}
		}
		if(!find){
			return false;
		}
		Node left = cursor.getLeft();
		Node right = cursor.getRight();
		Node orphan = voteRoot(cursor);
		if(!orphan.equals(left)) {
			orphan.setLeft(left);
		}
		if(!orphan.equals(right)){
			orphan.setRight(right);
		}
		parent.setRight(orphan);
		this.size--;
		return true;
	}

	private Node voteRoot(Node node){
		Node parent = node;

		Node left = node.getLeft();
		Node right = node.getRight();
		if(null == right){
			return left;
		}

		Node cursor = node.getRight();
		for(; null != cursor;){
			if(null == cursor.getLeft()){
				break;
			}
			parent = cursor;
			cursor = cursor.getLeft();
		}
		parent.setLeft(null);
		return cursor;
	}
}

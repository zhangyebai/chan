package com.core.chan.algorithm.tree;

import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class BinaryTree {

	private Node root;
	private int size = 0;

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	@Accessors(chain = true)
	@ToString
	public static final class Node{
		private Node left;
		private Node right;
		private int data;
	}

	public void insert(int value){
		Node node = new Node().setData(value);
		if(null == root){
			this.root = node;
			size ++;
			return;
		}
		Node index = this.root;
		for(;;){
			Node parent = index;
			///  turn left
			if(parent.getData() > value){
				index = parent.getLeft();
				if (null == index){
					parent.left = new Node().setData(value);
					size++;
					break;
				}
			}
			/// turn right
			else if (parent.getData() < value){
				index = parent.getRight();
				if(null == index){
					parent.setRight(new Node().setData(value));
					size ++;
					break;
				}
			}else{
				return;
			}
		}

	}

	public String pre(){
		if(null == this.root){
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder("[ ").append(this.root.getData());
		this.preErgodic(stringBuilder, this.root.left);
		this.preErgodic(stringBuilder, this.root.right);
		stringBuilder.append(" ]");
		return stringBuilder.toString();
	}

	private void preErgodic(StringBuilder stringBuilder, Node node){
		if(null == node){
			return;
		}
		stringBuilder.append(" ").append(node.getData());
		this.preErgodic(stringBuilder, node.getLeft());
		this.preErgodic(stringBuilder, node.getRight());
	}


	public String mid(){
		if(null == this.root){
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder("[");
		this.midErgodic(stringBuilder, this.root.getLeft());
		stringBuilder.append(" ").append(this.root.getData());
		this.midErgodic(stringBuilder, this.root.getRight());
		stringBuilder.append(" ]");
		return stringBuilder.toString();
	}

	private void midErgodic(StringBuilder stringBuilder, Node node){
		if(null == node){
			return;
		}
		midErgodic(stringBuilder, node.getLeft());
		stringBuilder.append(" ").append(node.getData());
		midErgodic(stringBuilder, node.getRight());
	}

	public String back(){
		if(null == this.root){
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder("[");
		this.backErgodic(stringBuilder, this.root.getLeft());
		this.backErgodic(stringBuilder, this.root.getRight());
		stringBuilder.append(" ").append(this.root.getData()).append(" ]");
		return stringBuilder.toString();
	}

	private void backErgodic(StringBuilder stringBuilder, Node node){
		if(null == node){
			return;
		}

		backErgodic(stringBuilder, node.getLeft());
		backErgodic(stringBuilder, node.getRight());
		stringBuilder.append(" ").append(node.getData());
	}

	public Node find(int value){
		if(null == this.root){
			return null;
		}
		Node result = null;
		Node index = this.root;

		for(; index != null; ){
			Node parent = index;
			/// turn left
			if(parent.getData() > value){
				index = parent.getLeft();
			}
			// turn right
			else if(parent.getData() < value){
				index = parent.getRight();
			}else{
				result = parent;
				break;
			}
		}
		return result;
	}

	/**
	 * @apiNote 二叉树的删除操作有一点需要注意的事情: 查找需要删除的节点的时候,
	 * 			需要查找该节点的父节点,以及要删除的目标节点是父节点的左子树还是右子树,
	 * 			简而言之就是需要目标节点及其父节点的信息
	 *
	 * step 1 : 查找要删除的节点以及父节点
	 * step 2 : 要删除的是叶节点
	 * step 3 : 要删除的是单叉子节点
	 * step 4 : 要删除的是二叉子节点
	 * */
	public boolean delete(int value){
		boolean result = false;
		if(null == this.root){
			return result;
		}
		Node index = this.root;
		Node parent = null;
		boolean isLeftNode = false;

		for(; index != null;){

			//turn left
			 if(index.getData() > value){
				 parent = index;
				index = index.getLeft();
				 isLeftNode = true;
			}
			//turn right
			else if(index.getData() < value){
				 parent = index;
			 	index = index.getRight();
				 isLeftNode = false;
			 }
			 else{
				break;
			 }
		}

		if(null == index){
			return false;
		}

		// 叶节点
		if(null == index.getLeft() && null == index.getRight()){
			if(index == this.root){
				this.root = null;
				--size;
				return true;
			}
			if(isLeftNode){
				parent.setLeft(null);
				--size;
				return true;
			}else{
				parent.setRight(null);
				--size;
				return true;
			}
		}else if (null == index.getLeft()){
			if(index == this.root){
				this.root = this.root.getRight();
				--size;
				return true;
			}

			if (isLeftNode){
				parent.setLeft(index.getRight());
				--size;
				return true;
			}else{
				parent.setRight(index.getRight());
				--size;
				return true;
			}
		}else if (null == index.getRight()){
			if(index == this.root){
				this.root = index.getLeft();
				--size;
				return true;
			}

			if(isLeftNode){
				parent.setLeft(index.getLeft());
				--size;
				return true;
			}else{
				parent.setRight(index.getLeft());
				--size;
				return true;
			}
		}else{
			Node master =  this.voteRoot(index);
			if(index == this.root){
				this.root = master;
				master.setLeft(index.getLeft());
				master.setRight(index.getRight());
				--size;
				return true;
			}

			if(isLeftNode){
				parent.setLeft(master);
			}else{
				parent.setRight(master);
			}

			master.setLeft(index.getLeft());
			master.setRight(index.getRight());
			--size;
			return true;
		}
	}


	/**
	 * orphan 孤儿
	 * 投票给孤儿的最右节点就行了
	 * */
	public Node voteRoot(Node orphan){
		Node index  = orphan.getRight();
		Node master = index;
		Node parent = null;

		for(; null != index && null != index.getLeft() ; ){
			master = index.getLeft();
			parent = index;
			index = index.getLeft();
		}
		if(null == parent && index == master){
			// it means orphan node's right node is a single node
			// orphan node's right node is master and root node, do nothing
		}else{
			parent.setLeft(null);
		}
		/// master.setRight(orphan.getRight());
		return master;
	}

}

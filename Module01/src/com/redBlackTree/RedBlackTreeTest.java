package com.redBlackTree;

public class RedBlackTreeTest {

	public static void main(String[] args) {
//		int[] arr = { 60, 56, 45, 68, 64, 66, 65, 58, 43, 72, 49, 62, 40 };
		int[] arr = { 640, 560, 660, 450, 600, 650, 680, 430, 490, 580, 620, 
				645, 655, 670, 690, 400, 440 }; //十倍后容易插入节点

// check result: https://www.cs.usfca.edu/~galles/visualization/RedBlack.html
		System.out.println("******add new node******");
		RedBlackTree RBTree = new RedBlackTree();
		for (int value : arr) {
			RBTree.add(new RedBlackTreeNode(value)); // 每加一个节点，都可能进行双旋转
		}
//		RBTree.add(new RedBlackTreeNode(73));
		System.out.println();
		System.out.println("******delete******");
		//每删除一个节点，都可能进行双旋转
		RBTree.delete(645);
		RBTree.delete(655);
		RBTree.delete(670);
		RBTree.delete(690);
		RBTree.delete(440);
		RBTree.delete(650);
		RBTree.delete(490);
		
		System.out.println("******print******");
		
		RBTree.inOrderList();
		System.out.println();
		System.out.println("avl3.root.height() = " + RBTree.root.height());
		System.out.println("avl3.root.leftHeight() = " + RBTree.root.leftHeight());
		System.out.println("avl3.root.rightHeight() = " + RBTree.root.rightHeight());
		System.out.println();
		
		System.out.println("print by height:");
		
		try{
			System.out.println(RBTree.root);
			System.out.println(RBTree.root.left); 
			System.out.println(RBTree.root.right);
			System.out.println(RBTree.root.left.left); 
			System.out.println(RBTree.root.left.right); 
			System.out.println(RBTree.root.right.left);
			System.out.println(RBTree.root.right.right);
	
			System.out.println();
			System.out.println(RBTree.root.left.left.left); 
			System.out.println(RBTree.root.left.left.right); 
			System.out.println(RBTree.root.left.right.left); 
			System.out.println(RBTree.root.left.right.right); 
			System.out.println(RBTree.root.right.left.left); 
			System.out.println(RBTree.root.right.left.right); 
			System.out.println(RBTree.root.right.right.left); 
			System.out.println(RBTree.root.right.right.right); 
			
			System.out.println();
			System.out.println(RBTree.root.left.left.left.left); 
			System.out.println(RBTree.root.left.left.left.right); 
			System.out.println(RBTree.root.right.left.left.left); 
			
		} catch (Exception e) {
			System.out.println("stop");
			System.out.println(e.getMessage());
		}
	}

}

class RedBlackTree{
	RedBlackTreeNode root;
	static final int BLACK = 1;
	static final int RED = 0;
	
	public void add(RedBlackTreeNode node){
		if(root == null){ // 对root特殊处理
			root = node;
			root.color = BLACK;
			return;
		}
		root.add(root, node);
	}
	
	public void delete(int value){ // delete
		if(root.value == value && root.left == null && root.right == null){
			root = null;
			return;
		}else if (root.value == value && root.left != null && root.right == null){
			root = root.left;
			root.color = BLACK;
			return;
		}else if (root.value == value && root.left == null && root.right != null){
			root = root.right;
			root.color = BLACK;
			return;
		}
		root.delete(root, value);
	}
	
	public void inOrderList(){
		if (root == null) {
			System.out.println("empty tree");
			return;
		}
		System.out.println("inOrderList:");
		root.inOrderList();
	}
}

class RedBlackTreeNode{
	int value;
	RedBlackTreeNode left;
	RedBlackTreeNode right;
	int color;
	static final int BLACK = 1;
	static final int RED = 0;
	
	public RedBlackTreeNode(int value) {
		this.value = value;
		this.color = RED;
	}
	
	public RedBlackTreeNode(RedBlackTreeNode node) {
		this.value = node.value;
		this.color = node.color;
	}

	public int leftHeight() {
		if (this.left != null) {
			return this.left.height();
		}
		return 0;
	}

	public int rightHeight() {
		if (this.right != null) {
			return this.right.height();
		}
		return 0;
	}

	public void leftRotate() {
		RedBlackTreeNode newNode = new RedBlackTreeNode(this);
		newNode.left = left;
		newNode.right = right.left;
		value = right.value;
		right = right.right;
		left = newNode;
		System.out.println("left rotate finished");
	}
	
	public void rightRotate() {
		RedBlackTreeNode newNode = new RedBlackTreeNode(this);
		newNode.right = right;
		newNode.left = left.right;
		value = left.value;
		left = left.left;
		right = newNode;
		System.out.println("right rotate finished");
	}
	
	public RedBlackTreeNode getUncle(RedBlackTreeNode parent, RedBlackTreeNode grandfather){
		if (grandfather != null){ //有爷爷节点
			if (grandfather.left != parent){
				return grandfather.left; // 包含null情况
			} else if (grandfather.right != parent){
				return grandfather.right; // 包含null情况
			}
		}
		
		return null;
	}
	
	public void add(RedBlackTreeNode root, RedBlackTreeNode node){
		if(node == null){
			return;
		}
		
		if(node.value < this.value){
			if(this.left == null){
				this.left = node;
				change_Color_Rotation(root, node);
			} else {
				this.left.add(root, node);
			}
		} else {
			if(this.right == null){
				this.right = node;
				change_Color_Rotation(root, node);
			} else {
				this.right.add(root, node);
			}
		}
	}
	
// 方法1
	public void change_Color_Rotation1(RedBlackTreeNode root, RedBlackTreeNode node){
		RedBlackTreeNode target = node;
		RedBlackTreeNode parent = root.searchParent(target.value); //肯定不是null，root已特殊处理
		RedBlackTreeNode grandfather = root.searchParent(parent.value);
		RedBlackTreeNode uncle = node.getUncle(parent, grandfather);
		while (parent != null){
			if (parent.color == BLACK){
				break;
			} else if (parent.color == RED){
				if (uncle != null && uncle.color == RED){ // 红父红叔，且有爷节点
					parent.color = BLACK;
//					System.out.println("parent: " + parent);
					uncle.color = BLACK;
//					System.out.println("uncle: " + uncle);
					grandfather.color = RED;
				} else if (uncle == null || uncle.color == BLACK){ // 红父黑叔，且有爷节点
					System.out.println("target: " + target);
					System.out.println("parent: " + parent);
					red_father_black_uncle(target, parent, grandfather);
//					System.out.println("*********red_father_black_uncle*********");
				}

				target = grandfather;
				parent = root.searchParent(target.value);
				
				if (parent != null){
					grandfather = root.searchParent(parent.value);
					uncle = node.getUncle(parent, grandfather);
				}
			}
//			System.out.println("loop");
		}
		root.color = BLACK;
	}

///* 方法2, 更简洁，red_father_black_uncle其实不用循环处理
	public void change_Color_Rotation(RedBlackTreeNode root, RedBlackTreeNode node){
		RedBlackTreeNode target = node;
		RedBlackTreeNode parent = root.searchParent(target.value); //肯定不是null，root已特殊处理
		if (parent.color == BLACK){
			// do nothing
		} else if (parent.color == RED){
			RedBlackTreeNode grandfather = root.searchParent(parent.value);
			RedBlackTreeNode uncle = node.getUncle(parent, grandfather);
			if (uncle != null && uncle.color == RED){ // 红父红叔，且有爷节点
				while (parent != null && parent.color == RED && 
						uncle != null && uncle.color == RED){
					parent.color = BLACK;
					uncle.color = BLACK;
					grandfather.color = RED;
					target = grandfather;
					parent = root.searchParent(target.value);
					if (parent != null){
						grandfather = root.searchParent(parent.value);
						uncle = node.getUncle(parent, grandfather);
					}
				}
				
				if (parent != null && parent.color == RED &&
						(uncle == null || uncle.color == BLACK) ){ // 红父黑叔，且有爷节点
					red_father_black_uncle(target, parent, grandfather);
				}
				
				root.color = BLACK;
			} else if (uncle == null || uncle.color == BLACK){ // 红父黑叔，且有爷节点
				red_father_black_uncle(target, parent, grandfather);
			}
		}
	}
// */
	public void red_father_black_uncle(RedBlackTreeNode target, RedBlackTreeNode parent, RedBlackTreeNode grandfather){
		if(grandfather.left == parent && parent.left == target){
			grandfather.rightRotate();
			grandfather.right.color = RED;
		} else if (grandfather.left == parent && parent.right == target){
			grandfather.left.leftRotate();
			grandfather.rightRotate();
			grandfather.right.color = RED;
		} else if (grandfather.right == parent && parent.right == target){
			grandfather.leftRotate();
			grandfather.left.color = RED;
		} else if (grandfather.right == parent && parent.left == target){
			grandfather.right.rightRotate();
			grandfather.leftRotate();
			grandfather.left.color = RED;
		}
	}
	
//可写入class BinarySortTree
	public void delete(RedBlackTreeNode root, int value){ // delete
		RedBlackTreeNode target = root.search(value); // 被删节点
		RedBlackTreeNode parent = root.searchParent(value); // 被删的父节点，在target是root时为null
		
		if(target != null){
			
			if(target.left == null && target.right == null){ // 情况1 & 2，此处不用考虑target是root的情况，上面已考虑
				// 因此parent不为空
				if(parent.left != null && target == parent.left){
					if(target.color == RED){ // 情况 1
						parent.left = null;
						System.out.println("left red leaf node successfully deleted");
					} else if (target.color == BLACK){ // 情况 2
						delete_BlackLeafNode(root, target, parent);
						System.out.println("left black leaf node successfully deleted");
					}
					System.out.println();
				}else if (parent.right != null && target == parent.right){
					if(target.color == RED){ // 情况 1
						parent.right = null;
						System.out.println("right red leaf node successfully deleted");
					} else if (target.color == BLACK){ // 情况 2
						delete_BlackLeafNode(root, target, parent);
						System.out.println("right black leaf node successfully deleted");
					}
					System.out.println();
				}
				
			} else if (target.left != null && target.right != null){ // target有二子；包含有两颗子节点的root这种情况
				RedBlackTreeNode max = getLeftSubtreeMaxNode(root, target.left);
				
				// 下面删除max节点
				if(target.left != max){ // target的左子节点 有 右子节点
					if(max.color == RED){ // max一定没有子节点
						int tempValue = max.value;
						root.searchParent(max.value).right = null; // 直接删掉red leaf LeftSubtreeMaxNode
						target.value = tempValue; // 这句必须放在最后，否则影响root.searchParent(max.value)，会错误地找到target的parent
						System.out.println("red leaf LeftSubtreeMaxNode successfully deleted");
					} else if(max.color == BLACK){
						if(max.left != null){ // max.left必为红子节点
							int tempValue = max.value;
							max.value = max.left.value;
							max.left = null;
							target.value = tempValue;
						} else if(max.left == null){ // max is black leaf
							int tempValue = max.value;
							target.value = tempValue;
							delete_BlackLeafNode(root, max, target.left);
							System.out.println("black leaf LeftSubtreeMaxNode successfully deleted");
						}
					}
				} else if(target.left == max){ // target的左子节点 无 右子节点
					if(max.color == RED){ // max一定没有子节点
						target.value = max.value;
						target.left = null; // 直接删掉red leaf LeftSubtreeMaxNode
						System.out.println("red leaf LeftSubtreeMaxNode successfully deleted (target.left == max)");
					} else if(max.color == BLACK){
						if(max.left != null){ // max.left必为红子节点
							target.value = max.value;
							max.value = max.left.value;
							max.left = null;
						} else if(max.left == null){ // max is black leaf
							int tempValue = max.value;
							target.value = tempValue;
							delete_BlackLeafNode(root, max, target);
							System.out.println("black leaf LeftSubtreeMaxNode successfully deleted (target.left == max)");
						}
					}
				}
				
				System.out.println("node with 2 sub-leaves successfully deleted");
			} else { // target的其中一个子节点为空
				if (target.left != null && target.color == BLACK){ // target的一个子节点必为红
					
					target.value = target.left.value;
					target.left = null;
					System.out.println("black node with 1 red left leaf successfully deleted");
				}else if(target.right != null && target.color == BLACK){ // target的一个子节点必为红
					
					target.value = target.right.value;
					target.right = null;
					System.out.println("black node with 1 red right leaf successfully deleted");
				}
			}
			
			root.color = BLACK; // root color definition
			
		} else {
			System.out.println("delete value(" + value + ") failed");
		}
	}
	
	public void delete_BlackLeafNode(RedBlackTreeNode root, RedBlackTreeNode target, RedBlackTreeNode parent){
		System.out.println("the most complex situation: delete black leaf node");
		RedBlackTreeNode brother;
		if(parent.right == target){
			brother = parent.left;
			if(brother.color == BLACK){
				rightTarget_LeftBlackBrother(root, brother, parent); //working
			} else if(brother.color == RED){
				parent.rightRotate();
				rightTarget_LeftBlackBrother(root, parent.right.left, parent.right);
			}
		} else if(parent.left == target){
			brother = parent.right;
			if(brother.color == BLACK){
				leftTarget_RightBlackBrother(root, brother, parent);
			} else if(brother.color == RED){
				parent.leftRotate();
				leftTarget_RightBlackBrother(root, parent.left.right, parent.left);
			}
		}
	}
	
	public void rightTarget_LeftBlackBrother(RedBlackTreeNode root, RedBlackTreeNode brother, RedBlackTreeNode parent){
		if(brother.left != null && brother.right != null){ // brother的两个子节点若都存在，就都是红节点
			parent.right = null;
			parent.rightRotate();
			parent.left.color = BLACK;
			parent.right.color = BLACK;
		} else if (brother.left != null && brother.right == null){ // 可以和上面的合并，不合并为了可读性
			parent.right = null;
			parent.rightRotate();
			parent.left.color = BLACK;
			parent.right.color = BLACK;
		} else if (brother.left == null && brother.right != null){ 
			parent.right = null;
			parent.left.leftRotate();
			parent.rightRotate();
			parent.left.color = BLACK;
			parent.right.color = BLACK;
		} else if (brother.left == null && brother.right == null){ 
			parent.right = null;
			parent.color++;
			brother.color = RED; // parent.left.color
			RedBlackTreeNode tempGrandfather;
			RedBlackTreeNode tempUncle;
			while(parent != root && parent.getColor() == "Double Black"){ // Double Black情况
				tempGrandfather = root.searchParent(parent);
				tempGrandfather.color++;
				parent.color--;
				if(tempGrandfather.right == parent){
					tempUncle = tempGrandfather.left;
					modifyColorForDoubleBlack_leftUncle(tempUncle, tempGrandfather);
				} else if(tempGrandfather.left == parent){
					tempUncle = tempGrandfather.right;
					modifyColorForDoubleBlack_rightUncle(tempUncle, tempGrandfather);
				}
				parent = tempGrandfather;
			}
//			System.out.println("checking " + parent);
//			System.out.println("checking " + root);
			// 最后要和前三种情况保持操作一致：
			parent.left.color = BLACK;
			parent.right.color = BLACK; 
		}
	}
	
	public void leftTarget_RightBlackBrother(RedBlackTreeNode root, RedBlackTreeNode brother, RedBlackTreeNode parent){
		if(brother.left != null && brother.right != null){ // brother的两个子节点若都存在，就都是红节点
			parent.left = null;
			parent.leftRotate();
			parent.left.color = BLACK;
			parent.right.color = BLACK;
		} else if (brother.right != null && brother.left == null){ // 可以和上面的合并，不合并为了可读性
			parent.left = null;
			parent.leftRotate();
			parent.left.color = BLACK;
			parent.right.color = BLACK;
		} else if (brother.right == null && brother.left != null){ 
			parent.left = null;
			parent.right.rightRotate();
			parent.leftRotate();
			parent.left.color = BLACK;
			parent.right.color = BLACK;
		} else if (brother.left == null && brother.right == null){ 
			parent.left = null;
			parent.color++;
			brother.color = RED; // parent.right.color
			RedBlackTreeNode tempGrandfather;
			RedBlackTreeNode tempUncle;
			while(parent != root && parent.getColor() == "Double Black"){ // Double Black情况
				tempGrandfather = root.searchParent(parent);
				tempGrandfather.color++;
				parent.color--;
				if(tempGrandfather.right == parent){
					tempUncle = tempGrandfather.left;
					modifyColorForDoubleBlack_leftUncle(tempUncle, tempGrandfather);
				} else if(tempGrandfather.left == parent){
					tempUncle = tempGrandfather.right;
					modifyColorForDoubleBlack_rightUncle(tempUncle, tempGrandfather);
				}
				parent = tempGrandfather;
			}
//			System.out.println("checking " + parent);
			// 最后要和前三种情况保持操作一致：
			parent.left.color = BLACK;
			parent.right.color = BLACK; 
		}
	}
	
	public void modifyColorForDoubleBlack_leftUncle(RedBlackTreeNode tempUncle, RedBlackTreeNode tempGrandfather){
		if(tempUncle.getColor() == "Black"){
			if(tempUncle.left.getColor() == "Black" && tempUncle.right.getColor() == "Black" ){
				tempUncle.color = RED;
			} else if(tempUncle.left.getColor() == "Red"){ 
				tempGrandfather.rightRotate();
				tempGrandfather.left.color = BLACK;
			} else if(tempUncle.left.getColor() == "Black"){ //tempUncle.right.color is RED
				tempUncle.leftRotate();
				tempGrandfather.rightRotate();
				tempGrandfather.left.color = BLACK;
			}
		} else if (tempUncle.getColor() == "Red"){ 
			RedBlackTreeNode leftNode = tempUncle.left; // must be black
			RedBlackTreeNode rightNode = tempUncle.right; // must be black
			redTempUncleFix_leftUncle(leftNode, tempUncle);
			redTempUncleFix_rightUncle(rightNode, tempUncle);
		}
	}
	
	public void modifyColorForDoubleBlack_rightUncle(RedBlackTreeNode tempUncle, RedBlackTreeNode tempGrandfather){
		if(tempUncle.getColor() == "Black"){
			if(tempUncle.left.getColor() == "Black" && tempUncle.right.getColor() == "Black" ){
				tempUncle.color = RED;
			} else if(tempUncle.right.getColor() == "Red"){ 
				tempGrandfather.leftRotate();
				tempGrandfather.right.color = BLACK;
			} else if(tempUncle.right.getColor() == "Black"){
				tempUncle.rightRotate();
				tempGrandfather.leftRotate();
				tempGrandfather.right.color = BLACK;
			}
		} else if (tempUncle.getColor() == "Red"){ 
			RedBlackTreeNode leftNode = tempUncle.left; // must be black
			RedBlackTreeNode rightNode = tempUncle.right; // must be black
			redTempUncleFix_leftUncle(leftNode, tempUncle);
			redTempUncleFix_rightUncle(rightNode, tempUncle);
		}
	}
	
	public void redTempUncleFix_leftUncle(RedBlackTreeNode tempUncle, RedBlackTreeNode tempGrandfather){
		if(tempUncle.left.getColor() == "Black" && tempUncle.right.getColor() == "Black" ){
			tempUncle.left.color = RED;
			tempUncle.right.color = RED;
		} else if(tempUncle.left.getColor() == "Red"){ 
			tempGrandfather.rightRotate();
			tempGrandfather.left.color = BLACK;
		} else if(tempUncle.left.getColor() == "Black"){
			tempUncle.leftRotate();
			tempGrandfather.rightRotate();
			tempGrandfather.left.color = BLACK;
		}
	}
	
	public void redTempUncleFix_rightUncle(RedBlackTreeNode tempUncle, RedBlackTreeNode tempGrandfather){
		if(tempUncle.left.getColor() == "Black" && tempUncle.right.getColor() == "Black" ){
			tempUncle.left.color = RED;
			tempUncle.right.color = RED;
		} else if(tempUncle.right.getColor() == "Red"){ 
			tempGrandfather.leftRotate();
			tempGrandfather.right.color = BLACK;
		} else if(tempUncle.left.getColor() == "Black"){
			tempUncle.rightRotate();
			tempGrandfather.leftRotate();
			tempGrandfather.right.color = BLACK;
		}
	}
	
	//可写入class BinarySortTree
	public RedBlackTreeNode getLeftSubtreeMaxNode(RedBlackTreeNode root, RedBlackTreeNode node){ // get以node为根节点的左子树最大值
		RedBlackTreeNode temp = node;
		while (temp.right != null){
			temp = temp.right;
		}
		return temp;
	}
	
	public RedBlackTreeNode search(int value){
		if(this.value == value){
			return this;
		} else if (value < this.value){
			if (this.left == null){
				return null;
			} else {
				return this.left.search(value);
			}
		} else {
			if (this.right == null){
				return null;
			} else {
				return this.right.search(value);
			}
		}
	}
	
	public RedBlackTreeNode searchParent(int value){
		if((this.left != null && this.left.value == value) ||
				(this.right != null && this.right.value == value)){
			return this;
		} else if (this.left != null && value < this.value){
			return this.left.searchParent(value);
		} else if (this.right != null && value >= this.value){
			return this.right.searchParent(value);
		} else {
			return null;
		}
	}
	
	public RedBlackTreeNode searchParent(RedBlackTreeNode node){
		if(this.left == node || this.right == node){
			return this;
		} else if (this.left != null && node.value < this.value){
			return this.left.searchParent(node);
		} else if (this.right != null && node.value >= this.value){
			return this.right.searchParent(node);
		} else {
			return null;
		}
	}
	
	public int height() {
		return Math.max(this.left == null? 0: left.height(), this.right == null? 0: right.height()) + 1;
	}
	
	public void inOrderList(){
		if(this.left != null){
			this.left.inOrderList();
		}
		
		System.out.println(this);
		
		if(this.right != null){
			this.right.inOrderList();
		}
	}
	
	@Override
	public String toString() {
		return "RedBlackTreeNode [ " + value + ", " + getColor() + " ]";
	}
	
	public String getColor(){
		if(this.color == 1){
			return "Black";
		} else if(this.color == 2){
			return "Double Black";
		} else if(this.color == 0){
			return "Red";
		} else {
			return "beyond color scope";
		}
	}

	public int numOfSubnode(RedBlackTreeNode node){
		int num = 0;
		if(node.left != null){
			num++;
		}
		if(node.right != null){
			num++;
		}
		return num;
	}
}
	
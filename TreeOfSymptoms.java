import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

class Symptom extends SymptomBase {

	public Symptom(String symptom, int severity) {
		super(symptom, severity);
	}

	@Override
	public int compareTo(SymptomBase o) {
		/* Add your code here! */
		if (this.getSeverity() > o.getSeverity()) {
			return 1;
		} else if (this.getSeverity() < o.getSeverity()) {
			return -1;
		} else {
			return 0;
		}
	}
}

public class TreeOfSymptoms extends TreeOfSymptomsBase {
	public ArrayList<SymptomBase> inOrderSymptoms = new ArrayList<>();
	public ArrayList<SymptomBase> postOrderSymptoms = new ArrayList<>();
	int start = 0;

	public TreeOfSymptoms(SymptomBase root) {
		super(root);
	}

	void inOrder(SymptomBase node) {
		if (node == null) {
			return;
		}
		// traverse the left child, node, and then right child
		inOrder(node.getLeft());
		if (!inOrderSymptoms.contains(node)) {
			inOrderSymptoms.add(node);
		}
		inOrder(node.getRight());

	}

	@Override
	public ArrayList<SymptomBase> inOrderTraversal() {
		/* Add your code here! */
		inOrder(this.getRoot());
		return inOrderSymptoms;
	}

	void postorder(SymptomBase node) {
		if (node == null) {
			return;
		}
		// traverse the left child, right child and then node
		postorder(node.getLeft());
		postorder(node.getRight());
		if (!inOrderSymptoms.contains(node)) {
			inOrderSymptoms.add(node);
		}
	}

	@Override
	public ArrayList<SymptomBase> postOrderTraversal() {
		/* Add your code here! */
		postorder(this.getRoot());
		return postOrderSymptoms;
	}

	// Reference Array-based tree in Data structure and algorithms
	SymptomBase listToTree(ArrayList<SymptomBase> symptoms, SymptomBase root, int start, int end) {
		int rootPosition = symptoms.indexOf(root);
		if (root == null){
			return null;
		}
		root.setLeft(listToTreeRest(symptoms, start, rootPosition-1));
		root.setRight(listToTreeRest(symptoms,rootPosition+1, end));
		return root;
	}

	// List to tree for the rest fo the tree other than root
	SymptomBase listToTreeRest(ArrayList<SymptomBase> symptoms, int start, int end) {
		if (start > end) {
			return null;
		}
		SymptomBase root = null;
		int mid = start + (end-start)/2;
		if (mid < symptoms.size()){
			root = symptoms.get(mid);
			root.setLeft(listToTreeRest(symptoms, start, mid-1));
			root.setRight(listToTreeRest(symptoms,mid+1, end));
		}
		return root;
	}

	SymptomBase findRoot(int threshold){
		ArrayList<SymptomBase> goals = new ArrayList<>();
		ArrayList<SymptomBase> orderedSymptoms = inOrderTraversal();
		int maxSeverity = 0;
		SymptomBase alterGoal = null;
		// If no such symptom (severity >= threshold) exists, use the most severe
		// symptom among existing as a new root node
		for (SymptomBase symptomBase: orderedSymptoms) {
			if (symptomBase.getSeverity() >= threshold) {
				goals.add(symptomBase);
			}
			if (symptomBase.getSeverity() > maxSeverity) {
				maxSeverity = symptomBase.getSeverity();
				alterGoal = symptomBase;
			}
		}
		// find symptom with the smallest severity that satisfies severity >= threshold
		if (!goals.isEmpty()) {
			int minSeverity = goals.get(0).getSeverity();
			SymptomBase goal = null;
			for (SymptomBase symptomBase: goals) {
				if (symptomBase.getSeverity() <= minSeverity) {
					minSeverity = symptomBase.getSeverity();
					goal = symptomBase;
				}
			}
			return goal;
		}
		return alterGoal;
	}

	@Override
	public void restructureTree(int threshold) {
		SymptomBase root = findRoot(threshold);
		if (root == null) {
			return;
		}
		this.inOrderSymptoms = inOrderTraversal();
		Collections.sort(this.inOrderSymptoms);
		SymptomBase newRoot = listToTree(this.inOrderSymptoms, root, start, inOrderSymptoms.size());
		this.setRoot(newRoot);
	}

	/* Add any extra functions below */
}

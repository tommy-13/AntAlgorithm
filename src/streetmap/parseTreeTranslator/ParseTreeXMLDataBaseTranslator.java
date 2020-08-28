package streetmap.parseTreeTranslator;

import io.parseTree.Leaf;
import io.parseTree.Node;
import io.parseTree.ParseTreeStructureException;
import io.parseTree.TreeElement;

import java.util.LinkedList;
import java.util.List;

import streetmap.model.DataBase;
import streetmap.model.Street;
import streetmap.model.StreetType;

public class ParseTreeXMLDataBaseTranslator {
	
	private static final String DATABASE	= "DATABASE";
	private static final String STREET		= "STREET";
	private static final String POS_X		= "POS_X";
	private static final String POS_Y		= "POS_Y";
	private static final String COST		= "COST";
	private static final String TYPE		= "TYPE";
	private static final String MARKED		= "MARKED";

	
	public static TreeElement createDataBaseTree() {
		DataBase dataBase = DataBase.getInstance();
		Node root = new Node(DATABASE);
		
		// safe streets
		List<Street> sl = dataBase.getAllStreets();
		for(Street s : sl) {
			root.addChild(createStreetNode(s));
		}
		
		return root;
	}
	
	private static TreeElement createStreetNode(Street s) {
		TreeElement leaf = new Leaf(STREET);
		leaf.setAttribute(POS_X, Integer.toString(s.getXPos()));
		leaf.setAttribute(POS_Y, Integer.toString(s.getYPos()));
		leaf.setAttribute(COST, Integer.toString(s.getCost()));
		leaf.setAttribute(TYPE, s.getStreetType().name());
		leaf.setAttribute(MARKED, Boolean.toString(s.isMarked()));
		return leaf;
	}
	
	
	
	public static void createDataBase(TreeElement root) throws ParseTreeStructureException {
		if(!root.getName().equals(DATABASE) || root.isLeaf()) {
			throw new ParseTreeStructureException("DataBase.Root");
		}
		
		Node nDataBase = (Node) root;
		
		List<Street> ls = new LinkedList<Street>();
		for(TreeElement e : nDataBase.getChildren(STREET)) {
			ls.add(createStreet(e));
		}
		
		DataBase.getInstance().load(ls);
	}
	
	private static Street createStreet(final TreeElement e) throws ParseTreeStructureException {
		if(!e.isLeaf()) {
			throw new ParseTreeStructureException("DataBase.Street");
		}
		Leaf leaf = (Leaf) e;
		
		if(!leaf.hasAttribute(POS_X)) {
			throw new ParseTreeStructureException("Street.PosX");
		}
		if(!leaf.hasAttribute(POS_Y)) {
			throw new ParseTreeStructureException("Street.PosY");
		}
		if(!leaf.hasAttribute(COST)) {
			throw new ParseTreeStructureException("Street.Cost");
		}
		if(!leaf.hasAttribute(TYPE)){
			throw new ParseTreeStructureException("Street.Type");
		}
		if(!leaf.hasAttribute(MARKED)) {
			throw new ParseTreeStructureException("Street.Marked");
		}
		
		int x, y, cost;
		try {
			x = Integer.parseInt(leaf.getAttribute(POS_X));
			y = Integer.parseInt(leaf.getAttribute(POS_Y));
			cost = Integer.parseInt(leaf.getAttribute(COST));
		} catch(NumberFormatException ex) {
			throw new ParseTreeStructureException("Street.ParseInt");
		}
		StreetType type = StreetType.valueOf(leaf.getAttribute(TYPE));
		boolean marked = Boolean.parseBoolean(leaf.getAttribute(MARKED));
		
		return new Street(type, x, y, cost, marked);
	}

}

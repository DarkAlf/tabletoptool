package com.t3.dice.expression;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

public class MultiplicationNode extends Expression {
	private ArrayList<Expression> nodes;
	private BitSet operations;

	public MultiplicationNode(Expression first) {
		nodes=new ArrayList<Expression>(10);
		nodes.add(first);
		operations=new BitSet();
		operations.set(nodes.size()-1, true);
	}

	public float getResult(Random random) {
		int product=1;
		for(int i=0;i<nodes.size();i++) {
			if(operations.get(i))
				product*=nodes.get(i).getResult(random);
			else
				product/=nodes.get(i).getResult(random);
		}
		return product;
	}

	public void multiplyBy(Expression term) {
		nodes.add(term);
		operations.set(nodes.size()-1, true);
	}
	
	public void divideBy(Expression term) {
		nodes.add(term);
		operations.set(nodes.size()-1, false);
	}
	

	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<nodes.size();i++) {
			if(i>0) {
				if(operations.get(i))
					sb.append('\u00D7');
				else
					sb.append('\u00F7');
			}
			if(operations.get(i))
				sb.append(nodes.get(i).toString());
			else
				sb.append(nodes.get(i).toString());
		}
		return sb.toString();
	}

	public String toEvaluatedString() {
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<nodes.size();i++) {
			if(i>0) {
				if(operations.get(i))
					sb.append('\u00D7');
				else
					sb.append('\u00F7');
			}
			if(operations.get(i))
				sb.append(nodes.get(i).toEvaluatedString());
			else
				sb.append(nodes.get(i).toEvaluatedString());
		}
		return sb.toString();
	}
}

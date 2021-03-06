/*
 * Copyright (c) 2014 tabletoptool.com team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     rptools.com team - initial implementation
 *     tabletoptool.com team - further development
 */
package com.t3.client.ui.zone.vbl;

import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.t3.GeometryUtil;
import com.t3.GeometryUtil.PointNode;

public class AreaMeta {
	Area area;
	Point2D centerPoint;
	List<AreaFace> faceList = new ArrayList<AreaFace>();

	// Only used during construction
	boolean isHole;
	PointNode pointNodeList;
	GeneralPath path;
	PointNode lastPointNode;

	public AreaMeta() {
	}

	public Point2D getCenterPoint() {
		if (centerPoint == null) {
			centerPoint = new Point2D.Double(area.getBounds().x + area.getBounds().width / 2, area.getBounds().y + area.getBounds().height / 2);
		}
		return centerPoint;
	}

	public Set<VisibleAreaSegment> getVisibleAreas(Point2D origin) {
		Set<VisibleAreaSegment> segSet = new HashSet<VisibleAreaSegment>();

		VisibleAreaSegment segment = null;
		for (AreaFace face : faceList) {
			double originAngle = GeometryUtil.getAngle(origin, face.getMidPoint());
			double delta = GeometryUtil.getAngleDelta(originAngle, face.getFacing());

			if (Math.abs(delta) > 90) {
				if (segment != null) {
					segSet.add(segment);
					segment = null;
				}
				continue;
			}
			// Continuous face
			if (segment == null) {
				segment = new VisibleAreaSegment(origin);
			}
			segment.addAtEnd(face);
		}
		if (segment != null) {
			// We finished the list while visible, see if we can combine with the first segment
			// TODO: attempt to combine with the first segment somehow
			segSet.add(segment);
		}
//		System.out.println("Segs: " + segSet.size());
		return segSet;
	}

	public Area getArea() {
		return new Area(area);
	}

	public boolean isHole() {
		return isHole;
	}

	public void addPoint(float x, float y) {
		// Cut out redundant points
		// TODO: This works ... in concept, but in practice it can create holes that pop outside of their parent bounds
		// for really thin diagonal lines.  At some point this could be moved to a post processing step, after the 
		// islands have been placed into their oceans.  But that's an optimization for another day
//		if (lastPointNode != null && GeometryUtil.getDistance(lastPointNode.point, new Point2D.Float(x, y)) < 1.5) {
//			skippedPoints++;
//			return;
//		}
		PointNode pointNode = new PointNode(new Point2D.Double(x, y));

		// Don't add if we haven't moved
		if (lastPointNode != null) {
			if (lastPointNode.point.equals(pointNode.point)) {
				return;
			}
		}
		if (path == null) {
			path = new GeneralPath();
			path.moveTo(x, y);

			pointNodeList = pointNode;
		} else {
			path.lineTo(x, y);

			lastPointNode.next = pointNode;
			pointNode.previous = lastPointNode;
		}
		lastPointNode = pointNode;
	}

	public void close() {
		area = new Area(path);

		// Close the circle
		lastPointNode.next = pointNodeList;
		pointNodeList.previous = lastPointNode;
		lastPointNode = null;

		// For some odd reason, sometimes the first and last point are the same, which causes
		// bugs in the way areas are calculated
		if (pointNodeList.point.equals(pointNodeList.previous.point)) {
			// Pull out the dupe node
			PointNode trueLastPoint = pointNodeList.previous.previous;
			trueLastPoint.next = pointNodeList;
			pointNodeList.previous = trueLastPoint;
		}
		computeIsHole();
		computeFaces();

		// Don't need point list anymore
		pointNodeList = null;
		path = null;
//		System.out.println("AreaMeta.skippedPoints: " + skippedPoints + " h:" + isHole + " f:" + faceList.size());
	}

	private void computeIsHole() {
		double angle = 0;

		PointNode currNode = pointNodeList.next;

		while (currNode != pointNodeList) {
			double currAngle = GeometryUtil.getAngleDelta(GeometryUtil.getAngle(currNode.previous.point, currNode.point), GeometryUtil.getAngle(currNode.point, currNode.next.point));

			angle += currAngle;
			currNode = currNode.next;
		}
		isHole = angle < 0;
	}

	private void computeFaces() {
		PointNode node = pointNodeList;
		do {
			faceList.add(new AreaFace(node.point, node.next.point));
			node = node.next;
		} while (!node.point.equals(pointNodeList.point));
	}
}

import javax.swing.JFrame
import java.awt.*
import java.awt.geom.Path2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.util.List

static double angleOf(Point2D.Double a, Point2D.Double b) {
	double rad = Math.atan2(b.y - a.y, b.x - a.x)
	return Math.toDegrees(rad) + 180
}

def input = getClass().getResource('/day9.txt').readLines()
def coords = input*.split(',')
		.collect { it*.toInteger() as Point2D.Double }
		.sort { a, b -> a.y <=> b.y ?: a.x <=> b.x }

def rects = [] as List<Rectangle2D>
for (i in 0..<(coords.size() - 1)) {
	def a = coords[i]
	for (j in (i + 1)..<coords.size()) {
		def b = coords[j]
		rects << new Polygon(
				[a.x, b.x, b.x, a.x] as int[],
				[a.y, a.y, b.y, b.y] as int[],
				4
		).bounds2D
	}
}

def largeA = rects.max { it.width * it.height as long }
println "Largest (any): $largeA (Area ${(largeA.width + 1) * (largeA.height + 1) as long})"

def width = coords*.x.max() + 2
def height = coords*.y.max() + 2

def poly = new Path2D.Double()
def center = coords.removeFirst()
poly.moveTo(center.x as double, center.y as double)

def vtx = coords.toList()
while (!vtx.empty) {
	def next = null as Point2D.Double
	double minAngle = Integer.MAX_VALUE

	for (i in 0..<(vtx.size())) {
		def coord = vtx[i]
		double angle = angleOf(center, coord)
		if (angle % 90 == 0 && angle < minAngle) {
			minAngle = angle
			next = coord

			if (minAngle == 0) break
		}
	}

	vtx.remove(center = next)
	poly.lineTo(next.x as double, next.y as double)
}
poly.closePath()

def largeB = rects
		.findAll { poly.contains(it) }
		.max { it.width * it.height as long }

println "Largest (in bounds): $largeB (Area ${(largeB.width + 1) * (largeB.height + 1) as long})"

// DATA RENDERING FOR VISUALIZATION
double scale = 1 / 200
def frame = new JFrame()
frame.size = [width * scale + 50 as int, height * scale + 50 as int]
frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
frame.visible = true
def g = frame.contentPane.graphics as Graphics2D
g.scale(scale, scale)
g.stroke = new BasicStroke(1 / scale * 3 as float)
g.draw(poly)

coords.each {
	g.color = Color.ORANGE
	g.draw(new Rectangle2D.Double(it.x - 0.5, it.y - 0.5, 1, 1))
}

g.stroke = new BasicStroke(1 / scale as float)

g.color = Color.GREEN
g.draw(largeA)

g.color = Color.RED
g.draw(largeB)
// DATA RENDERING FOR VISUALIZATION

public enum SpawnPoint {
	//               dx    dy  rot        cent off
	NORTH      (0,    0,  3.5, -90, 2, 1, -15, -20), 
	SOUTH      (2,    0, -3.5,  90, 2, 1,   5,  20),
	EAST_TOP   (1, -3.5,    0,   0, 3, 1, -15, -20),
	EAST_BOTTOM(4, -3.5,    0,   0, 3, 2, -15, -20),
	WEST_TOP   (3,  3.5,    0, 180, 3, 1,   5,  20),
	WEST_BOTTOM(5,  3.5,    0, 180, 3, 2,   5,  20);
	
	private int spawnpoint;
	private double dx;
	private double dy;
	private double rotation;
	private int lane_divisor;
	private int lane_multiplier;
	private int lane_center;
	private int lane_offset;

	private SpawnPoint(int spawnPoint, double dx, double dy, double rotation, int lane_divisor, int lane_multiplier, int lane_center, int lane_offset) {
		this.spawnpoint = spawnPoint;
		this.dx = dx;
		this.dy = dy;
		this.rotation = rotation;
		this.lane_divisor = lane_divisor;
		this.lane_multiplier = lane_multiplier;
		this.lane_center = lane_center;
		this.lane_offset = lane_offset;
	}

	public double getStartXPos(int turn, int mapWidth) {
		double result = 0;
		switch(this) {
		case NORTH:
			result = mapWidth/lane_divisor + lane_center;
			if(turn == 0) {
				result += lane_offset;
			}
			break;
		case SOUTH:
			result = mapWidth/lane_divisor + lane_center;
			if(turn == 0) {
				result += lane_offset;
			}
			break;
		case EAST_TOP:
		case EAST_BOTTOM:
			result = mapWidth;
			break;
		}
		System.out.println("X POS = " + result);
		return result;
	}
	
	public double getStartYPos(int turn, int mapHeight) {
		double result = 0;
		switch(this) {
		case SOUTH:
			result = mapHeight;
			break;
		case EAST_TOP:
		case EAST_BOTTOM:
		case WEST_TOP:
		case WEST_BOTTOM:
			result = lane_multiplier * mapHeight/lane_divisor + lane_center;
			if(turn == 0) {    
				result += lane_offset;
			}
			break;
		}
		System.out.println("Y POS = " + result);
		return result;
	}
	
	public static SpawnPoint randomSpawnPoint() {
		SpawnPoint result =  toValue((int)(Math.random()*6));
		System.out.println("Spawned at " + result);
		return result;
	}

	public static SpawnPoint toValue(int i) {
		SpawnPoint result = null;
		switch(i) {
		case 0:
			result = SpawnPoint.NORTH; break;
		case 1: 
			result = SpawnPoint.EAST_TOP; break;
		case 2:
			result = SpawnPoint.SOUTH; break;
		case 3: 
			result = SpawnPoint.WEST_TOP; break;
		case 4:
			result = SpawnPoint.EAST_BOTTOM; break;
		case 5:
			result = SpawnPoint.WEST_BOTTOM; break;
		}
		return result;
	}

	public int getSpawnpoint() {
		return spawnpoint;
	}

	public double getDx() {
		return dx;
	}

	public double getDy() {
		return dy;
	}

	public double getRotation() {
		return rotation;
	}
}

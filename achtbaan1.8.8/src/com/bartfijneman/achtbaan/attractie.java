package com.bartfijneman.achtbaan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class attractie 
{
	private String name;
	private String runCommand;
	private String runPermissions;
	private String spawnCommand;
	private String spawnPermissions;
	
	public boolean allowSit;
	public boolean allowLeave;
	
	private HashMap<String, List<String>> spawn;
	private HashMap<String, List<String>> run;
	
	private HashMap<Integer, HashMap<Integer, ArmorStand>> cars;
	private HashMap<Integer, Double> carsR;
	
	private HashMap<Player, ArmorStand> passagiers = new HashMap<Player, ArmorStand>();
	
	public void addPassagier(Player p, ArmorStand A){
		passagiers.put(p, A);
	}
	public void removePassagier(Player p){
		passagiers.remove(p);
		Main.r.Attracties.put(this.name, this);
	}
	public HashMap<Player, ArmorStand> getPassagiers(){
		return passagiers;
	}
	
	public ArrayList<ArmorStand> getSeats(){
		ArrayList<ArmorStand> result = new ArrayList<ArmorStand>();
		for(HashMap<Integer, ArmorStand> car : cars.values()){
			result.add(car.get(2));
			result.add(car.get(3));
		}
		return result;
	}
	public ArrayList<ArmorStand> getMainSeats(){
		ArrayList<ArmorStand> result = new ArrayList<ArmorStand>();
		for(HashMap<Integer, ArmorStand> car : cars.values()){
			result.add(car.get(1));
		}
		return result;
	}
	
	public attractie(String Name, String runcommand, String runpermissions, String spawncommand, String spawnpermissions, HashMap<String, List<String>> spawnlist, HashMap<String, List<String>> runlist){
		this.name = Name;
		this.runCommand = runcommand;
		this.runPermissions = runpermissions;
		this.spawnCommand = spawncommand;
		this.spawnPermissions = spawnpermissions;
		this.spawn = spawnlist;
		this.run = runlist;
		
		cars = new HashMap<Integer, HashMap<Integer, ArmorStand>>();
		carsR = new HashMap<Integer, Double>();
	}
	public boolean checkIfIdExists(int id){
		return (cars.containsKey(id) ? true : false);
	}
	public boolean checkIfSpawnIdExists(String id){
		return (spawn.containsKey(id) ? true : false);
	}
	public boolean checkIfRunIdExists(String id){
		return (run.containsKey(id) ? true : false);
	}
	public String getName(){
		return this.name;
	}
	public String getRunCommand(){
		return this.runCommand;
	}
	public String getRunPermissions(){
		return this.runPermissions;
	}
	public String getSpawnCommand(){
		return this.spawnCommand;
	}
	public String getSpawnPermissions(){
		return this.spawnPermissions;
	}
	public HashMap<String, List<String>> getSpawn(){
		return this.spawn;
	}
	public HashMap<String, List<String>> getRun(){
		return this.run;
	}
	public void runSpawn(int id, String runid){
		runLoop(this.spawn.get(runid), 0, id);
	}
	public void runRun(int id, String runid){
		runLoop(this.run.get(runid), 0, id);
	}
	private void runLoop(final List<String> list, int next, final int id)
	{
		for(int i = next; i < list.size(); i++)
		{
			String line = list.get(i);
			if(line.contains(":")){
				String[] lineS = line.split(":");
				if(lineS[0].equals("run")){
					run(lineS[1], id);
				}
				if(lineS[0].equals("wait")){
					final int current = i;
					Main.r.getServer().getScheduler().runTaskLater(Main.r, new Runnable()
				    {
				      	public void run()
				      	{
				      		runLoop(list, current+1, id);
				      	}
				    }, Integer.parseInt(lineS[1]));
					return;
				}
			}
		}
	}
	private void run(String function, int id){
		String functionName = function.split(" ")[0];
		String functionValues = function.split(" ")[1];
		String[] values = functionValues.split(",");
		
		if(functionName.equals("spawn")){
			this.carsR.put(id, Double.parseDouble(values[8]));
			
			ArmorStand A1 = (ArmorStand) Bukkit.getWorld(values[0]).spawnEntity(new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Integer.parseInt(values[4]), 0), EntityType.ARMOR_STAND);
			A1.setHelmet(new ItemStack(Material.valueOf(values[6].toUpperCase()), 0, (byte)Integer.parseInt(values[7])));
			A1.setVisible(false);
			A1.setBasePlate(false);
			A1.setGravity(false);
			A1.setCustomName(A1.getUniqueId().toString());
			
			HashMap<Integer, ArmorStand> car = new HashMap<Integer, ArmorStand>();
			
			car.put(1, A1);
			
			int amount = (values.length > 9 ? Integer.parseInt(values[9]) : 2);
			for(int i = 0; i < amount; i++)
			{
				ArmorStand A2 = (ArmorStand) Bukkit.getWorld(values[0]).spawnEntity(new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3])), EntityType.ARMOR_STAND);
				A2.setBasePlate(false);
				A2.setVisible(false);
				A2.setGravity(false);
				A2.setCustomName(A2.getUniqueId().toString());
				
				car.put(2+i, A2);
			}
			
			this.cars.put(id, car);
			
			run__A(id);
			
			
			Main.r.Attracties.put(this.name, this);
		}
		if(functionName.equals("move")){
			HashMap<Integer, ArmorStand> car = cars.get(id);
			
			ArmorStand A1 = car.get(1);
			A1.teleport(new Location(
					car.get(1).getWorld(), 
					car.get(1).getLocation().getX() + Double.parseDouble(values[0]), 
					car.get(1).getLocation().getY() + Double.parseDouble(values[1]), 
					car.get(1).getLocation().getZ() + Double.parseDouble(values[2]),
					car.get(1).getLocation().getYaw() + Integer.parseInt(values[3]),
					0));
			String command = String.format("tp @e[name=%s] %s %s %s %s %s", 
					A1.getUniqueId().toString(), 
					A1.getLocation().getX(), 
					A1.getLocation().getY(), 
					A1.getLocation().getZ(), 
					A1.getLocation().getYaw(), 
					A1.getLocation().getPitch());
	        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			
	        String command1 = String.format("entitydata @e[name=%s] {Pose:{Head:[%sf,0f,%sf]}}", A1.getUniqueId().toString(), A1.getHeadPose().getX() + -(Double.parseDouble(values[4])), A1.getHeadPose().getZ() + (values.length == 6 ? Double.parseDouble(values[5]) : 0));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command1);
	        
            A1.setHeadPose(A1.getHeadPose().add(-(Double.parseDouble(values[4])), 0, (values.length == 6 ? Double.parseDouble(values[5]) : 0)));
            
			run__A(id);
			
			
			Main.r.Attracties.put(this.name, this);
		}
		if(functionName.equals("eject")){
			for(ArmorStand A : getSeats()){
				if(A.getPassenger() != null){
					Entity pass = A.getPassenger();
					A.eject();
					pass.teleport(new Location(A.getWorld(), 
							A.getLocation().getX()+Double.parseDouble(values[0]),
							A.getLocation().getY()+Double.parseDouble(values[1]),
							A.getLocation().getZ()+Double.parseDouble(values[2]),
							Integer.parseInt(values[3]),
							Integer.parseInt(values[4])
							));
				}
			}
		}
		if(functionName.equals("allow_sit")){
			if(values[0].equals("true")){
				this.allowSit = true;
			}else{
				this.allowSit = false;
			}
			Main.r.Attracties.put(this.name, this);
		}
		if(functionName.equals("allow_leave")){
			if(values[0].equals("true")){
				this.allowLeave = true;
			}else{
				this.allowLeave = false;
			}
			Main.r.Attracties.put(this.name, this);
		}
		if(functionName.equals("remove")){
			HashMap<Integer, ArmorStand> car = cars.get(id);
			for(ArmorStand as : car.values())
			{
				String command = String.format("kill @e[name=%s]", as.getUniqueId().toString());
		        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
			this.carsR.remove(id);
			this.cars.remove(id);
			Main.r.Attracties.put(this.name, this);
		}
	}
	private void run__A(int id){
		HashMap<Integer, ArmorStand> car = cars.get(id);
		double radius = carsR.get(id);
		int total = car.size()-1;
		
		ArmorStand A1 = car.get(1);
		
		for(int i = 1; i < total+1; i++)
		{
			ArmorStand A2 = car.get(i+1);
			
			{
				double angle = ((A1.getLocation().getYaw()+(360.0/total*i))*Math.PI / 180);
		        double x = radius*Math.cos(angle);
		        double z = radius*Math.sin(angle);
		        
		        String command = String.format("tp @e[name=%s] %s %s %s %s %s", A2.getUniqueId().toString(), A1.getLocation().getX() + x, A1.getLocation().getY(), A1.getLocation().getZ() + z, A1.getLocation().getYaw(), A1.getLocation().getPitch());
		        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
		}
	}
	public void remove__A()
	{
		for(HashMap<Integer, ArmorStand> car : cars.values())
		{
			for(ArmorStand as : car.values())
			{
				String command = String.format("kill @e[name=%s]", as.getUniqueId().toString());
		        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
		}
	}
}

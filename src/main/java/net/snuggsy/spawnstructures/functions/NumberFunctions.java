package net.snuggsy.spawnstructures.functions;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Objects;

import static net.minecraft.world.level.GameRules.RULE_SPAWN_RADIUS;
import static net.snuggsy.spawnstructures.data.GlobalVariables.LOGGER;
import static net.snuggsy.spawnstructures.data.GlobalVariables.newLog;
import static net.snuggsy.spawnstructures.functions.BlockPosFunctions.getHeighestBlock;
import static net.snuggsy.spawnstructures.functions.GenerationFunctions.getBiomeViaCommand;

public class NumberFunctions {

    public static boolean isNumeric(String string) {
        if (string == null) {
            return false;
        }
        try {
            Double.parseDouble(string);
        }
        catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static BlockPos convertCoordString(ServerLevel serverLevel, String specifiedLocation, String mode) {
        int arrayLength;
        if (Objects.equals(mode, "XZ") || Objects.equals(mode, "XZ-C")) {
            arrayLength = 2;
        } else if (Objects.equals(mode, "XYZ")) {
            arrayLength = 3;
        } else {
            LOGGER.error("[Spawn Structures] Specified Location threw an incorrect conversion mode. Please report this to the issue tracker.");
            return null;
        }
        if (specifiedLocation.contains("[") && specifiedLocation.contains("]") && specifiedLocation.contains(",")) {
            String[] coords;
            try {
                if (specifiedLocation.contains(":")) {
                    specifiedLocation = specifiedLocation.split(":", 2)[1];
                }
                String rawcoords = specifiedLocation.split("\\[")[1].split("]")[0];
                if (rawcoords.contains(", ")) {
                    coords = rawcoords.split(", ");
                } else {
                    coords = rawcoords.split(",");
                }
            } catch (IndexOutOfBoundsException ex) {
                LOGGER.error("[Spawn Structures] Specified Location threw an IndexOutOfBoundsException. Please adjust your specified location in the config file.");
                return null;
            }
            if (coords.length == arrayLength) {
                String sx = coords[0];
                String sy = coords[1];
                String sz = coords[arrayLength - 1];
                if (Objects.equals(sy, "~")) {
                    sy = "64";
                }
                if (isNumeric(sx) && isNumeric(sy) && isNumeric(sz)) {
                    if (mode.equals("XZ")) {
                        return getHeighestBlock(serverLevel, Integer.parseInt(sx), Integer.parseInt(sz));
                    } else if (mode.equals("XZ-C")) {
                        return new BlockPos(Integer.parseInt(sx), 0, Integer.parseInt(sz)); // pY is actually an offset
                    } else {
                        return new BlockPos(Integer.parseInt(sx), Integer.parseInt(sy), Integer.parseInt(sz));
                    }
                }
            }
        } else {
            LOGGER.error("[Spawn Structures] Specified Location was not provided in correct format. Please adjust your specified location in the config file.");
        }
        return null;
    }

    public static Pair<Integer, Integer> getRndStructureCoordinates() {
        // Controllers
        double k = 4.25 * Math.PI; // T-Limits
        double a = 8.0;            // T-Multiplier
        double b = 1.4;            // T-Power
        double r = 0.8;            // T-Restrict
        // Pi Values
        double c;
        int c1 = (int)(4 * Math.random());
        c = (double) c1 / 2;
        // Randomizer
        double t;
        double f = Math.random()/Math.nextDown(1.0);
        double f1 = (-k)*(1.0-f)+(k * f);
        if (f1 <= r && f1 >= -r) {
            f = Math.random()/Math.nextDown(1.0);
            double f2 = (-k)*(1.0-f)+(k * f);
            if (f2 <= r && f2 >= -r) {
                f = Math.random()/Math.nextDown(1.0);
                double f3 = (-k)*(1.0-f)+(k * f);
                if (f3 <= r && f3 >= -r) {
                    f = Math.random()/Math.nextDown(1.0);
                    t = (-k)*(1.0-f)+(k * f);
                    //t = 3 * (f1 + f2 + f3);
                } else {
                    t = f3;
                }
            } else {
                t = f2;
            }
        } else {
            t = f1;
        }
        // Coordinate Calculator
        double fX = (a * Math.pow(t,b)) * Math.cos(t + (c * Math.PI));
        double fZ = (a * Math.pow(t,b)) * Math.sin(t + (c * Math.PI));

        return new Pair<>((int) fX, (int) fZ);
    }

    public static Integer getRndSpawnRadius(ServerLevel serverLevel) {
        int k = serverLevel.getGameRules().getRule(RULE_SPAWN_RADIUS).get();
        newLog("Spawn Radius: " + k);
        double f = Math.random();
        double f1 = (-k)*(1.0-f)+(k * f);
        return (int) f1;
    }

    public static Integer get3DCoordDist(BlockPos startLocation, BlockPos endLocation) {
        int a = startLocation.getX() - endLocation.getX();
        int b = startLocation.getY() - endLocation.getX();
        int c = startLocation.getZ() - endLocation.getZ();
        if (a < 0) {a += 2 * a;}
        if (b < 0) {b += 2 * b;}
        if (c < 0) {c += 2 * c;}
        double d = Math.pow(a, 2) + Math.pow(b, 2) +  Math.pow(c, 2);
        double dist = Math.sqrt(d);
        return (int) dist;
    }

    public static BlockPos triangulateBiome(ServerLevel serverLevel, BlockPos startLocation, BlockPos endLocation, String biome, boolean logInfo) {
        if (startLocation == endLocation) {
            return endLocation;
        }
        int scanDist = 128;
        double r = getBearingInDegrees(startLocation, endLocation);
        double r1 = convertBearingToRadians(r - 30.0D);
        double r2 = convertBearingToRadians(r + 30.0D);
        BlockPos d1 = getCoordFromBearing(startLocation, endLocation, scanDist, r1);
        BlockPos d2 = getCoordFromBearing(startLocation, endLocation, scanDist, r2);
        if (logInfo) {
        newLog("First position at: " + endLocation);
        newLog("Angle to First position: " + r);
        }
        String nextBiomeCoords = CommandFunctions.getRawCommandOutput(serverLevel, Vec3.atBottomCenterOf(d1), "/locate biome " + biome);
        BlockPos nextPos1 = convertCoordString(serverLevel, nextBiomeCoords, "XYZ");
        if (logInfo) {
            newLog("Next position at: " + nextPos1);
            newLog("Angle to Next position: " + r1);
        }
        //newLog("Angle to Next position: " + Math.toDegrees(r1));
        nextBiomeCoords = CommandFunctions.getRawCommandOutput(serverLevel, Vec3.atBottomCenterOf(d2), "/locate biome " + biome);
        BlockPos nextPos2 = convertCoordString(serverLevel, nextBiomeCoords, "XYZ");
        if (logInfo) {
            newLog("Last position at: " + nextPos2);
            newLog("Angle to Last position: " + r2);
        }
        //newLog("Angle to Last position: " + Math.toDegrees(r2));
        assert nextPos1 != null;
        assert nextPos2 != null;
        BlockPos centrePos = new BlockPos((endLocation.getX()+nextPos1.getX()+nextPos2.getX())/3, (endLocation.getY()+nextPos1.getY()+nextPos2.getY())/3,(endLocation.getZ()+nextPos1.getZ()+nextPos2.getZ())/3);
        if (Objects.equals(biome, getBiomeViaCommand(serverLevel, centrePos, "overworld"))) {
            return centrePos;
        } else {
            return endLocation;
        }
    }

    public static Double getBearingInDegrees(BlockPos startPos, BlockPos endPos) {
        int x1 = startPos.getX();
        int z1 = startPos.getZ();
        int x2 = endPos.getX();
        int z2 = endPos.getZ();
        if (x1 == x2 || z1 == z2) {
            if (z1 > z2) {
                return 0.0D;
            } else if (x2 > x1) {
                return 90.0D;
            } else if (z2 > z1) {
                return 180.0D;
            } else {
                return 270.0D;
            }
        } else if (x2 > x1) {
            if (z1 > z2) {
                return 90.0D + Math.toDegrees(Math.atan((double) (z1 - z2) /(x2 - x1)));
            } else {
                return 90.0D - Math.toDegrees(Math.atan((double) (z2 - z1) /(x2 - x1)));
            }
        } else {
            if (z1 > z2) {
                return 270.0D - Math.toDegrees(Math.atan((double) (z1 - z2) /(x1 - x2)));
            } else {
                return 270.0D + Math.toDegrees(Math.atan((double) (z2 - z1) /(x1 - x2)));
            }
        }
    }

    public static Double convertBearingToRadians(Double r) {
        if (r > 360.0D) {
            r = 90.0D - (r - 360.0D);
        } else if (r > 270.0D) {
            r = r - 270.0D;
        } else if (r > 180.0D) {
            r = 270.0D - r;
        } else if (r > 90.0D) {
            r = r - 90.0D;
        } else if (r > 0.0D) {
            r = 90.0D - r;
        } else {
            r = r + 90.0D;
        }
        return Math.toRadians(r);
    }

    public static BlockPos getCoordFromBearing(BlockPos startPos, BlockPos endPos, Integer scanDist, Double r) {
        int x1 = startPos.getX();
        int z1 = startPos.getZ();
        int x2 = endPos.getX();
        int z2 = endPos.getZ();
        int newX = 0;
        int newZ = 0;
        if (x2 != x1) {
            newX = (int)(scanDist * Math.sin(r));
        }
        if (z2 != z1) {
            newZ = (int)(scanDist * Math.cos(r));
        }

        if (x2 > x1) {
            newX = endPos.getX() + newX;
        } else if (x1 > x2) {
            newX = endPos.getX() - newX;
        }
        if (z2 > z1) {
            newZ = endPos.getZ() + newZ;
        } else if (z1 > z2) {
            newZ = endPos.getZ() - newZ;
        }
        return new BlockPos(newX, endPos.getY(), newZ);
    }

    public static Integer getSmallestIntIndex(ArrayList<Integer> n) {
        int sIndex = 0;
        for (int i = 0; i < n.size(); i++) {
            if (n.get(sIndex) > n.get(i)) {
                sIndex = i;
            }
        }
        return sIndex;
    }

    public static BlockPos getOptimalHeight(ServerLevel serverLevel, BlockPos centrePos, Integer scanDist) {
        int centreX = centrePos.getX();
        int centreZ = centrePos.getZ();
        BlockPos k0 = getHeighestBlock(serverLevel, centreX, centreZ);
        BlockPos k1 = getHeighestBlock(serverLevel, centreX+scanDist, centreZ+scanDist);
        BlockPos k2 = getHeighestBlock(serverLevel, centreX-scanDist, centreZ+scanDist);
        BlockPos k3 = getHeighestBlock(serverLevel, centreX+scanDist, centreZ-scanDist);
        BlockPos k4 = getHeighestBlock(serverLevel, centreX-scanDist, centreZ-scanDist);
        return new BlockPos(centreX, (k0.getY()+k1.getY()+k2.getY()+k3.getY()+k4.getY())/5, centreZ);
    }
}

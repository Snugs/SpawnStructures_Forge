package net.snuggsy.spawnstructures.functions;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import static net.minecraft.world.level.GameRules.RULE_SPAWN_RADIUS;
import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.functions.BlockPosFunctions.getHeighestBlock;

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

    public static BlockPos convertSpecifiedLocation(ServerLevel serverLevel, String specifiedLocation) {
        if (specifiedLocation.contains("[") && specifiedLocation.contains("]") && specifiedLocation.contains(",")) {
            String[] coords;
            try {
                String rawcoords = specifiedLocation.split("\\[")[1].split("]")[0];
                coords = rawcoords.split(",");
            } catch (IndexOutOfBoundsException ex) {
                LOGGER.error("[Spawn Structures] Specified Location threw an IndexOutOfBoundsException. Please adjust your specified location in the config file.");
                return null;
            }
            if (coords.length == 2) {
                String sx = coords[0];
                String sz = coords[1];
                if (isNumeric(sx) && isNumeric(sz)) {
                    return getHeighestBlock(serverLevel, Integer.parseInt(sx), Integer.parseInt(sz));
                }
            }
        } else {
            LOGGER.error("[Spawn Structures] Specified Location was not provided in correct format. Please adjust your specified location in the config file.");
        }
        return null;
    }

    public static BlockPos getRndStructureCoordinates() {
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
                    t = 3 * (f1 + f2 + f3);
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

        return new BlockPos((int) fX, 0, (int) fZ);
    }

    public static Integer getRndSpawnRadius(ServerLevel serverLevel) {
        int k = serverLevel.getGameRules().getRule(RULE_SPAWN_RADIUS).get();
        newLog("Spawn Radius: " + k);
        double f = Math.random();
        double f1 = (-k)*(1.0-f)+(k * f);
        return (int) f1;
    }
}

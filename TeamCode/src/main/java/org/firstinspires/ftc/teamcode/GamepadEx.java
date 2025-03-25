package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class GamepadEx {
    public enum GamepadButton {
        A,
        B,
        X,
        Y,
        RIGHT_TRIGGER,
        LEFT_TRIGGER,
        RIGHT_BUMPER,
        LEFT_BUMPER,
        DPAD_LEFT,
        DPAD_RIGHT,
        DPAD_UP,
        DPAD_DOWN,
        LEFT_STICK,
        RIGHT_STICK,
        ALWAYS
    }
    public static GamepadEx Primary;
    public static GamepadEx Secondary;

    public HashMap<GamepadButton[], BiConsumer<Gamepad, Gamepad>> bindings = new HashMap<>();
    public HashMap<GamepadButton[], BiConsumer<Gamepad, Gamepad>> bindings_alt = new HashMap<>();

    private Gamepad gamepad;
    private Gamepad prev;

    public static void init_gamepads(Gamepad g1, Gamepad g2)  {
        Primary = new GamepadEx(g1);
        Secondary = new GamepadEx(g2);
    }

    public GamepadEx(Gamepad gp) {
        gamepad = gp;
    }

    public void bind(GamepadButton btn, BiConsumer<Gamepad, Gamepad> callback) {
        GamepadButton[] arr = new GamepadButton[]{btn};
        if(bindings.containsKey(arr)) {
            return;
        }
        bindings.put(arr, callback);
    }

    public void bind(GamepadButton btn, BiConsumer<Gamepad, Gamepad> down, BiConsumer<Gamepad, Gamepad> up) {
        bind(btn, down);
        bind_alt(btn, up);
    }

    public void bind(BiConsumer<Gamepad, Gamepad> callback, GamepadButton... btns) {
        GamepadButton[] arr = (GamepadButton[]) Arrays.stream(btns).toArray();
        if(bindings.containsKey(arr)) {
            return;
        }
        bindings.put(arr, callback);
    }

    public void bind_alt(GamepadButton btn, BiConsumer<Gamepad, Gamepad> callback) {
        GamepadButton[] arr = new GamepadButton[]{btn};
        if(bindings_alt.containsKey(arr)) {
            return;
        }
        bindings_alt.put(arr, callback);
    }

    public void bind_alt(BiConsumer<Gamepad, Gamepad> callback, GamepadButton... btns) {
        GamepadButton[] arr = (GamepadButton[]) Arrays.stream(btns).toArray();
        if(bindings_alt.containsKey(arr)) {
            return;
        }
        bindings_alt.put(arr, callback);
    }

    public void update_gamepad(Gamepad gp) {
        prev.copy(gamepad);
        gamepad.copy(gp);

    }

    public void update() {
        for(Map.Entry<GamepadButton[], BiConsumer<Gamepad, Gamepad>> entry : bindings.entrySet()) {
            if (Arrays.stream(entry.getKey()).anyMatch(this::get_button_state)) {
                entry.getValue().accept(gamepad, prev);
            }
        }
        for(Map.Entry<GamepadButton[], BiConsumer<Gamepad, Gamepad>> entry : bindings_alt.entrySet()) {
            if (Arrays.stream(entry.getKey()).noneMatch(this::get_button_state)) {
                entry.getValue().accept(gamepad, prev);
            }
        }
    }

    public void update(Gamepad new_gamepad) {
        update_gamepad(new_gamepad);
        update();
    }

    public boolean get_button_state(GamepadButton btn) {
        switch (btn) {
            case A:
                return gamepad.a;
            case B:
                return gamepad.b;
            case X:
                return gamepad.x;
            case Y:
                return gamepad.y;
            case RIGHT_TRIGGER:
                return gamepad.right_trigger != 0;
            case LEFT_TRIGGER:
                return gamepad.left_trigger != 0;
            case DPAD_LEFT:
                return gamepad.dpad_left;
            case DPAD_UP:
                return gamepad.dpad_up;
            case DPAD_DOWN:
                return gamepad.dpad_down;
            case DPAD_RIGHT:
                return gamepad.dpad_right;
            case RIGHT_BUMPER:
                return gamepad.right_bumper;
            case LEFT_BUMPER:
                return gamepad.left_bumper;
            case LEFT_STICK:
                return gamepad.left_stick_x != 0 || gamepad.left_stick_y != 0;
            case RIGHT_STICK:
                return gamepad.right_stick_x != 0 || gamepad.right_stick_y != 0;
            case ALWAYS:
                return true;
        }
        return true;
    }

    public boolean[] button_array() {
        return new boolean[] { gamepad.a, gamepad.b, gamepad.x, gamepad.y};
    }

    public enum GamepadColour {
        RED(255, 0, 0),
        GREEN(0, 255, 0),
        BLUE(0, 0, 255),
        BLACK(0, 0, 0),
        WHITE(255, 255, 255),
        CYAN(0, 255, 255),
        PURPLE(127, 0, 255),
        YELLOW(255, 255, 0),
        ORANGE(255, 128, 0),
        PINK(255, 0, 127),

        DARK_RED(102, 0, 0),
        DARK_YELLOW(102, 102, 0),
        DARK_GREEN(0, 102, 0),
        DARK_BLUE(0, 0, 102),
        DARK_CYAN(0, 102, 102),

        LIGHT_RED(255, 153, 153),
        LIGHT_YELLOW(255, 255, 153),
        LIGHT_GREEN(153, 255, 153),
        LIGHT_CYAN(153, 255, 255),
        LILAC(153, 153, 255),
        LIGHT_PINK(255, 153, 204);


        private final int red;
        private final int green;
        private final int blue;

        /**
         * Set up colour
         * @param r Red Value
         * @param g Green Value
         * @param b Blue Value
         */
        GamepadColour(int r, int g, int b) {
            this.red = r;
            this.green = g;
            this.blue = b;
        }
    }

    /**
     * Switch for if Gamepad should rumble on certain actions.
     * IE. Colour switch
     */
    public static boolean GamepadRumble = true;

    /**
     * Sets light on gamepad and rumbles
     * @param gp Gamepad to modify colour
     * @param col Colour to change gamepad to
     */
    public static void SetGamepadLight(Gamepad gp, GamepadColour col) {
        if(GamepadRumble) gp.rumble(200);
        gp.setLedColor(col.red, col.green, col.blue, Gamepad.LED_DURATION_CONTINUOUS);
    }
}
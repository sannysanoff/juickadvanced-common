/*
 * Juick
 * Copyright (C) 2008-2012, Ugnich Anton
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.juickadvanced.data.juick;

import java.util.Vector;

/**
 *
 * @author Ugnich Anton
 */
public class JuickPlace {

    public int pid = 0;
    public double lat = 0;
    public double lon = 0;
    public String name = null;
    public String description = null;
    public int users = 0;
    public int messages = 0;
    public int distance = 0;
    public String source = null;
    public Vector<String> tags = new Vector<String>();

}

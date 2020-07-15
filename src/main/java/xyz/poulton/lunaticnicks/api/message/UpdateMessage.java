//    Copyright © Lucy Poulton 2020.
//    This file is part of LunaticNicks.
//
//    LunaticNicks is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    LunaticNicks is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with LunaticNicks. If not, see <https://www.gnu.org/licenses/>.

package xyz.poulton.lunaticnicks.api.message;

import net.md_5.bungee.api.chat.BaseComponent;
import java.util.UUID;

public class UpdateMessage
{
    public UUID uuid;
    public BaseComponent[] nick;
    
    public UpdateMessage(final UUID uuid, final BaseComponent[] nick) {
        this.uuid = uuid;
        this.nick = nick;
    }
}

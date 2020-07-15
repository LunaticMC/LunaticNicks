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

package xyz.poulton.lunaticnicks.spigot;

import xyz.poulton.lunaticnicks.api.message.UpdateMessage;
import xyz.poulton.lunaticnicks.api.message.RequestMessage;
import xyz.poulton.lunaticnicks.api.message.MessageResponder;

public class SpigotMessageResponder implements MessageResponder
{
    LunaticNicksSpigot pl;
    
    public SpigotMessageResponder(final LunaticNicksSpigot pl) {
        this.pl = pl;
    }
    
    @Override
    public void onRequestMessage(final RequestMessage message) {
    }
    
    @Override
    public void onUpdateMessage(final UpdateMessage message) {
        this.pl.nickCache.remove(message.uuid);
        this.pl.nickCache.put(message.uuid.toString(), message.nick);
    }
}

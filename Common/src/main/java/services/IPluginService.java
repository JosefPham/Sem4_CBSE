/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import data.GameData;
import data.World;

/**
 *
 * @author Peter
 */
public interface IPluginService {
    
    void start(GameData gameData, World world);

    void stop(GameData gameData, World world);
}

package com.debla.hashpet.services;

import android.content.Context;

import java.util.Map;

/**
 * Created by Dave-PC on 2017/12/23.
 */

public interface IPetService {
    public void init(Context context);
    public void createNewPet(Map params);
}

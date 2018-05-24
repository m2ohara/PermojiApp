package com.permoji.user;

import android.os.Bundle;
import android.os.Trace;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.permoji.api.trait.Trait;
import com.permoji.model.UserTrait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setUserTraitsAdapter();

    }

    private void setUserTraitsAdapter() {

        ListView view = (ListView) findViewById(R.id.user_trait_list);

        UserTraitAdapter userTraitAdapter = new UserTraitAdapter(this, R.layout.user_trait_list, getTraits());

        view.setAdapter(userTraitAdapter);
    }

    private List<UserTrait> getTraits() {
        //TODO: Load user traits
        UserTrait uTrait1 = new UserTrait();
        Trait trait1 = new Trait(); trait1.setDescription("CHEERFUL"); trait1.setCodepoint(0x1F600); trait1.setAmount(4);
        uTrait1.setTrait(trait1);


        UserTrait uTrait2 = new UserTrait();
        Trait trait2 = new Trait(); trait2.setDescription("CLASS CLOWN"); trait2.setCodepoint(0x1F602); trait2.setAmount(7);
        uTrait2.setTrait(trait2);

        List<UserTrait> traits = new ArrayList<>(Arrays.asList(uTrait1, uTrait2));


        for(int idx = 0; idx < 5; idx++) {
            UserTrait uTrait = new UserTrait();
            Trait trait = new Trait(); trait.setDescription("CLASS CLOWN"); trait.setCodepoint(0x1F602); trait.setAmount(7);
            uTrait.setTrait(trait);
            traits.add(uTrait);
        }



        return traits;
    }

}

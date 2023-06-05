package com.theflexproject.thunder.fragments.homeNew

import com.theflexproject.thunder.model.MyMedia

data class ParentModel (
    val title : String = "",
    val children : List<MyMedia>
)
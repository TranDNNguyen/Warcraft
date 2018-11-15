# ECS160Android
# ECS 160 Project Android Version

## Working Working Working , Implementing UI fragmentation, Added FragmentManager in MainAcrivity

    static FragmentManager fragManager;
    
1. We set it to Static so that It can be accessible from other class to change the contents of fragments, more freedom is better you know.
2. It handles all the fragments

### Example

    FragmentThree fragment = (FragmentThree) MainActivity_viewport.fragManager.findFragmentById(R.id.fragment3);
    fragment.peasantSelected();
You can Call function in a Fragment by this way.

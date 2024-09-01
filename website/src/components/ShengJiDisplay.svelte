<script lang="ts">
    import WorksOn from "../sheng-ji-display/sections/WorksOn.svelte";
    import Foss from "../sheng-ji-display/sections/FOSS.svelte";
    import LevelUp from "../sheng-ji-display/sections/LevelUp.svelte";
    import NextLevel from "../sheng-ji-display/sections/NextLevel.svelte";
    import PossibleTrumps from "../sheng-ji-display/sections/PossibleTrumps.svelte";
    import Teammates from "../sheng-ji-display/sections/Teammates.svelte";
    import TrumpCalls from "../sheng-ji-display/sections/TrumpCalls.svelte";

    let scroll = 0;
    let height: number;
</script>

<svelte:window bind:scrollY={scroll} bind:innerHeight={height} />

<LevelUp />
<TrumpCalls />
<PossibleTrumps />
<Teammates />
<WorksOn />
<Foss />
<NextLevel />

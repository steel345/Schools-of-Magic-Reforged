#!/usr/bin/env bash
set -e
cd src/main/resources/assets/som/models/item

write_sub() { echo "{ \"parent\": \"som:block/$2\" }" > "$1.json"; }
write_main() {
  # $1=id, $2=default block model, $3=overrides body
  cat > "$1.json" <<EOF
{
  "parent": "som:block/$2",
  "overrides": [
$3
  ]
}
EOF
}

# ---- FaeStone-family helper ----
gen_fae_family() {
  local id=$1; shift
  local default_bm=$1; shift
  local -a vars=("$@")  # variant=bm pairs like "normal:fae_stone_normal"
  local ovr=""
  local n=${#vars[@]}
  local i=0
  for pair in "${vars[@]}"; do
    local v=${pair%%:*}
    local bm=${pair##*:}
    local p
    p=$(awk -v i=$i -v n=$n 'BEGIN{printf "%.4f",(i+0.5)/n}')
    ovr+="    { \"predicate\": { \"som:fae_variant\": $p }, \"model\": \"som:item/${id}_${v}\" },\n"
    write_sub "${id}_${v}" "$bm"
    i=$((i+1))
  done
  ovr=$(printf '%b' "${ovr%,\\n*},")
  ovr=${ovr%,}
  write_main "$id" "$default_bm" "$ovr"
}

gen_fae_family fae_stone fae_stone_normal \
  "normal:fae_stone_normal" "cobble:fae_cobble" "mossy_cobble:fae_mossy_cobble" \
  "bricks:fae_bricks" "mossy_bricks:fae_mossy_bricks" \
  "cracked_bricks:fae_cracked_bricks" "chiseled_bricks:fae_chiseled_bricks"
gen_fae_family gypsum gypsum_normal \
  "normal:gypsum_normal" "cobble:gypsum_cobble" "mossy_cobble:gypsum_mossy_cobble" \
  "bricks:gypsum_bricks" "mossy_bricks:gypsum_mossy_bricks" \
  "cracked_bricks:gypsum_cracked_bricks" "chiseled_bricks:gypsum_chiseled_bricks"
gen_fae_family mud_marble mud_marble_a \
  "normal:mud_marble_a" "cobble:mud_marble_cobble" "mossy_cobble:mud_marble_mossy_cobble" \
  "bricks:mud_marble_bricks" "mossy_bricks:mud_marble_mossy_bricks" \
  "cracked_bricks:mud_marble_cracked_bricks" "chiseled_bricks:mud_marble_chiseled_bricks"

# ---- Standard ore family ----
gen_ore_family() {
  local id=$1 prefix=$2
  local -a ORE=(coal diamond emerald gold iron lapis silver copper)
  local ovr=""
  local i=0
  for v in "${ORE[@]}"; do
    local p
    p=$(awk -v i=$i 'BEGIN{printf "%.4f",(i+0.5)/8}')
    ovr+="    { \"predicate\": { \"som:standard_ore\": $p }, \"model\": \"som:item/${id}_${v}\" },\n"
    write_sub "${id}_${v}" "${prefix}_${v}"
    i=$((i+1))
  done
  ovr=$(printf '%b' "${ovr%,\\n*},")
  ovr=${ovr%,}
  write_main "$id" "${prefix}_coal" "$ovr"
}
gen_ore_family fae_ore fae_ore
gen_ore_family gypsum_ore gypsum_ore
gen_ore_family mud_marble_ore mud_marble_ore

# ---- Metal family (16 metals, 8 fall back to copper) ----
METAL=(silver tarnished_silver copper patinated_copper bronze antique_bronze brass aged_brass steel tenebrium abrasium magnillion eonium celestium simulite iridion)
gen_metal_family() {
  local id=$1 suffix=$2 fallback=$3
  local ovr=""
  local i=0
  for v in "${METAL[@]}"; do
    local p
    p=$(awk -v i=$i 'BEGIN{printf "%.4f",(i+0.5)/16}')
    ovr+="    { \"predicate\": { \"som:metal_variant\": $p }, \"model\": \"som:item/${id}_${v}\" },\n"
    case "$v" in
      steel|tenebrium|abrasium|magnillion|eonium|celestium|simulite|iridion)
        write_sub "${id}_${v}" "$fallback" ;;
      *)
        write_sub "${id}_${v}" "${v}_${suffix}" ;;
    esac
    i=$((i+1))
  done
  ovr=$(printf '%b' "${ovr%,\\n*},")
  ovr=${ovr%,}
  write_main "$id" "$fallback" "$ovr"
}
gen_metal_family metal_block  block  copper_block
gen_metal_family metal_bricks bricks copper_bricks

# ---- Hardened clay bricks (3 sub-blocks x 16 colors) ----
HCB_ELEMENT=(pyromancy heliomancy aeromancy geomancy animancy electromancy hydromancy cryomancy hieromancy chaotics auramancy astromancy infernality spectromancy umbramancy necromancy)
HCB_COLOR_pyromancy=red; HCB_COLOR_heliomancy=orange; HCB_COLOR_aeromancy=yellow
HCB_COLOR_geomancy=lime; HCB_COLOR_animancy=green; HCB_COLOR_electromancy=cyan
HCB_COLOR_hydromancy=lblue; HCB_COLOR_cryomancy=blue; HCB_COLOR_hieromancy=purple
HCB_COLOR_chaotics=magenta; HCB_COLOR_auramancy=pink; HCB_COLOR_astromancy=white
HCB_COLOR_infernality=lgray; HCB_COLOR_spectromancy=gray; HCB_COLOR_umbramancy=black
HCB_COLOR_necromancy=brown
hcb_color() { local v="HCB_COLOR_$1"; echo "${!v}"; }
gen_hcb_family() {
  local id=$1 suffix=$2  # suffix empty | crack | chisel
  local ovr=""
  local i=0
  for el in "${HCB_ELEMENT[@]}"; do
    local color=$(hcb_color "$el")
    local p
    p=$(awk -v i=$i 'BEGIN{printf "%.4f",(i+0.5)/16}')
    ovr+="    { \"predicate\": { \"som:magic_type\": $p }, \"model\": \"som:item/${id}_${el}\" },\n"
    local bm
    if [ -z "$suffix" ]; then bm="hardened_clay_bricks_${color}"; else bm="hardened_clay_bricks_${color}_${suffix}"; fi
    write_sub "${id}_${el}" "$bm"
    i=$((i+1))
  done
  ovr=$(printf '%b' "${ovr%,\\n*},")
  ovr=${ovr%,}
  local default
  if [ -z "$suffix" ]; then default="hardened_clay_bricks_red"; else default="hardened_clay_bricks_red_${suffix}"; fi
  write_main "$id" "$default" "$ovr"
}
gen_hcb_family hardened_clay_bricks          ""
gen_hcb_family hardened_clay_bricks_cracked  crack
gen_hcb_family hardened_clay_bricks_chiseled chisel

# ---- Rotted planks (12 woods) ----
ROTTED=(oak spruce birch jungle acacia dark_oak ash elder pine willow yew verde)
ovr=""
i=0
for w in "${ROTTED[@]}"; do
  p=$(awk -v i=$i 'BEGIN{printf "%.4f",(i+0.5)/12}')
  ovr+="    { \"predicate\": { \"som:bookshelf_wood\": $p }, \"model\": \"som:item/rotted_planks_${w}\" },\n"
  write_sub "rotted_planks_${w}" "rotted_planks_${w}"
  i=$((i+1))
done
ovr=$(printf '%b' "${ovr%,\\n*},")
ovr=${ovr%,}
write_main "rotted_planks" "rotted_planks_oak" "$ovr"

# ---- Planks (6 SOM woods, no underscore in block model names) ----
PLANKS_W=(ash elder pine willow yew verde)
ovr=""
i=0
for w in "${PLANKS_W[@]}"; do
  p=$(awk -v i=$i 'BEGIN{printf "%.4f",(i+0.5)/6}')
  ovr+="    { \"predicate\": { \"som:wood_type\": $p }, \"model\": \"som:item/planks_${w}\" },\n"
  write_sub "planks_${w}" "planks${w}"
  i=$((i+1))
done
ovr=$(printf '%b' "${ovr%,\\n*},")
ovr=${ovr%,}
write_main "planks" "planksash" "$ovr"

# ---- Planter (12 woods, abbreviated names) ----
PLANTER_W=(oak spruce birch jungle acacia dark_oak ash elder pine willow yew verde)
declare -A PBM=(
  [oak]=planteroak [spruce]=planterspru [birch]=planterbirch [jungle]=planterjun
  [acacia]=planteraca [dark_oak]=planterdoak [ash]=planterash [elder]=planterelder
  [pine]=planterpine [willow]=planterwill [yew]=planteryew [verde]=planterverd)
ovr=""
i=0
for w in "${PLANTER_W[@]}"; do
  p=$(awk -v i=$i 'BEGIN{printf "%.4f",(i+0.5)/12}')
  ovr+="    { \"predicate\": { \"som:bookshelf_wood\": $p }, \"model\": \"som:item/planter_${w}\" },\n"
  write_sub "planter_${w}" "${PBM[$w]}"
  i=$((i+1))
done
ovr=$(printf '%b' "${ovr%,\\n*},")
ovr=${ovr%,}
write_main "planter" "planteroak" "$ovr"

echo "ALL DONE"
ls -1 fae_stone*.json gypsum*.json mud_marble*.json metal_*.json hardened_clay_bricks*.json rotted_planks*.json planks*.json planter*.json 2>/dev/null | wc -l

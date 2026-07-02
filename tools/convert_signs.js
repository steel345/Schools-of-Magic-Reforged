const zlib=require("zlib"),fs=require("fs");
function parse(buf){let p=0;
 const u8=()=>buf[p++], i8=()=>buf.readInt8(p++);
 const u16=()=>{let v=buf.readUInt16BE(p);p+=2;return v;}, i16=()=>{let v=buf.readInt16BE(p);p+=2;return v;};
 const i32=()=>{let v=buf.readInt32BE(p);p+=4;return v;}, i64=()=>{let v=buf.readBigInt64BE(p);p+=8;return v;};
 const f32=()=>{let v=buf.readFloatBE(p);p+=4;return v;}, f64=()=>{let v=buf.readDoubleBE(p);p+=8;return v;};
 const str=()=>{let n=u16();let s=buf.toString("utf8",p,p+n);p+=n;return s;};
 function payload(t){switch(t){
  case 1:return i8();case 2:return i16();case 3:return i32();case 4:return i64();case 5:return f32();case 6:return f64();
  case 7:{let n=i32();let a=[];for(let i=0;i<n;i++)a.push(i8());return a;}
  case 8:return str();
  case 9:{let et=u8();let n=i32();let items=[];for(let i=0;i<n;i++)items.push({t:et,v:payload(et)});return {et,items};}
  case 10:{let arr=[];while(true){let tt=u8();if(tt===0)break;let name=str();arr.push({name,t:tt,v:payload(tt)});}return arr;}
  case 11:{let n=i32();let a=[];for(let i=0;i<n;i++)a.push(i32());return a;}
  case 12:{let n=i32();let a=[];for(let i=0;i<n;i++)a.push(i64());return a;}
 }}
 let t=u8();let name=str();let v=payload(t);return {t,name,v};
}
function write(root){let chunks=[];
 const B=b=>chunks.push(Buffer.from([b&0xff]));
 const U16=v=>{let b=Buffer.alloc(2);b.writeUInt16BE(v);chunks.push(b);};
 const I16=v=>{let b=Buffer.alloc(2);b.writeInt16BE(v);chunks.push(b);};
 const I32=v=>{let b=Buffer.alloc(4);b.writeInt32BE(v);chunks.push(b);};
 const I64=v=>{let b=Buffer.alloc(8);b.writeBigInt64BE(BigInt(v));chunks.push(b);};
 const F32=v=>{let b=Buffer.alloc(4);b.writeFloatBE(v);chunks.push(b);};
 const F64=v=>{let b=Buffer.alloc(8);b.writeDoubleBE(v);chunks.push(b);};
 const STR=s=>{let b=Buffer.from(s,"utf8");U16(b.length);chunks.push(b);};
 function payload(t,v){switch(t){
  case 1:B(v);break;case 2:I16(v);break;case 3:I32(v);break;case 4:I64(v);break;case 5:F32(v);break;case 6:F64(v);break;
  case 7:I32(v.length);for(const x of v)B(x);break;
  case 8:STR(v);break;
  case 9:B(v.et);I32(v.items.length);for(const it of v.items)payload(v.et,it.v);break;
  case 10:for(const e of v){B(e.t);STR(e.name);payload(e.t,e.v);}B(0);break;
  case 11:I32(v.length);for(const x of v)I32(x);break;
  case 12:I32(v.length);for(const x of v)I64(x);break;
 }}
 B(root.t);STR(root.name);payload(root.t,root.v);return Buffer.concat(chunks);
}
const find=(c,n)=>c.find(e=>e.name===n);
function convertSigns(file){
 let root=parse(zlib.gunzipSync(fs.readFileSync(file)));
 let blocks=find(root.v,"blocks"); if(!blocks)return 0; let n=0;
 for(const blk of blocks.v.items){
   let nbtE=find(blk.v,"nbt"); if(!nbtE)continue; let comp=nbtE.v; let idE=find(comp,"id");
   if(!idE||!String(idE.v).includes("sign"))continue;
   let ts=["Text1","Text2","Text3","Text4"].map(k=>find(comp,k));
   if(ts.every(e=>!e))continue;
   const msgs=ts.map(e=>({t:8,v:(e&&e.v)?e.v:'{"text":""}'}));
   const blank=[0,0,0,0].map(()=>({t:8,v:'{"text":""}'}));
   const txt=(items)=>[{name:"messages",t:9,v:{et:8,items}},{name:"color",t:8,v:"black"},{name:"has_glowing_text",t:1,v:0}];
   let kept=comp.filter(e=>!["Text1","Text2","Text3","Text4","front_text","back_text","is_waxed"].includes(e.name));
   kept.push({name:"is_waxed",t:1,v:0});
   kept.push({name:"front_text",t:10,v:txt(msgs)});
   kept.push({name:"back_text",t:10,v:txt(blank)});
   nbtE.v=kept; n++;
 }
 if(n>0) fs.writeFileSync(file,zlib.gzipSync(write(root)));
 return n;
}
for(const nm of ["warning","puzzel_room","puzzel_trap_hard","puzzle_easy","puzzle_hard","mini_pyromancy"]){
  const f="src/main/resources/data/som/structures/"+nm+".nbt";
  try{console.log(nm+": converted "+convertSigns(f)+" signs");}catch(e){console.log(nm+": ERROR "+e.message);}
}
